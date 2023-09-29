package com.example.moneyflow.ui.fragments.create

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moneyflow.R
import com.example.moneyflow.databinding.FragmentCreateNewTransactionBinding
import com.example.moneyflow.ui.base.BaseScreen
import com.example.moneyflow.ui.ui_state.CreateTransactionItemUIState
import com.example.moneyflow.ui.ui_state.PermissionDialogUIState
import com.example.moneyflow.domain.model.Category
import com.example.moneyflow.utils.DataPicker
import com.example.moneyflow.utils.ImageStorageManager
import com.example.moneyflow.utils.ReadImageAndVideoPermissionMessageProvider
import com.example.moneyflow.utils.showPermissionDialog
import com.example.moneyflow.ui.viewmodels.PermissionsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date


class CreateTransactionFragment : Fragment(), BaseScreen {
    private var _binding: FragmentCreateNewTransactionBinding? = null
    private val binding get() = _binding!!

    private val amountEditText by lazy { binding.amountEditText }
    private val categoryInputText by lazy { binding.categoryInputText }
    private val categoryAutoComplete by lazy { binding.categoryAutoComplete }
    private val dateEditText by lazy { binding.dateEditText }
    private val image by lazy { binding.imageTransaction }

    private val createTransactionViewModel: CreateTransactionViewModel by activityViewModels()
    private val permissionsViewModel: PermissionsViewModel by activityViewModels()

    private val dataPicker = DataPicker()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateNewTransactionBinding.inflate(inflater, container, false)

        setAllListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI(createTransactionViewModel.uiItemState.value)

        viewLifecycleOwner.lifecycleScope.launch {
            observeCreateTransactionUIState()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun observeCreateTransactionUIState() {
        withContext(Dispatchers.Default) {
            createTransactionViewModel.uiState.collect { uiState ->
                if(uiState.isCreated){
                    updateUI(createTransactionViewModel.uiItemState.value)
                }
            }
        }
    }

    private val openGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            createTransactionViewModel.setImage(it)
            image.setImageURI(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, it))
                ImageStorageManager().saveToInternalStorage(requireContext(), bitmap )
            } else {
                TODO("VERSION.SDK_INT < P")
            }

        }
    }

    private val readMediaImagePermissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        permissionsViewModel.onPermissionResult(Manifest.permission.READ_MEDIA_IMAGES, isGranted)

        permissionsViewModel.permissionDialogQueue.reversed().forEach {
            showReadMediaImagePermissionDialog(it)
        }
    }

    private fun showReadMediaImagePermissionDialog(permission: String) {
        val permissionDialogUIState = PermissionDialogUIState(
            title = "Grant Permission",
            message = when (permission) {
                Manifest.permission.READ_MEDIA_IMAGES -> {
                    ReadImageAndVideoPermissionMessageProvider()
                }

                else -> {return}
            },
            isPermanentlyDeclined = shouldShowRequestPermissionRationale(permission),
            onDismissClick = permissionsViewModel::dismissDialog,
            onOkClick = {
                readMediaImagePermissionResultLauncher.launch(permission)
                permissionsViewModel::dismissDialog
            },
            onGoToAppSettingsClick = ::goToAppSettings
        )

        MaterialAlertDialogBuilder(requireContext()).showPermissionDialog(permissionDialogUIState)
    }

    private fun goToAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        ).also(::startActivity)
    }

    private fun updateUI(uiState: CreateTransactionItemUIState) {
        amountEditText.setText(uiState.amount?.toString())
        categoryAutoComplete.setText(uiState.category?.name)
        categoryAutoComplete.setSimpleItems(createTransactionViewModel.uiItemState.value.listOfCategory.toTypedArray())
        categoryInputText.setStartIconDrawable(uiState.category?.icon ?: R.drawable.ic_category)
        dateEditText.setText(dataPicker.currentDay(Date(uiState.date)))
        image.setImageURI(uiState.image)
    }

    private fun setAllListeners() {
        amountEditText.addTextChangedListener(onTextChangedListener())

        categoryAutoComplete.setOnItemClickListener { _, _, _, id ->
            createTransactionViewModel.setCategory(Category.values()[id.toInt()])
            categoryInputText.setStartIconDrawable(createTransactionViewModel.uiItemState.value.category!!.icon)
        }

        dateEditText.setOnClickListener {
            showDatePicker()
        }

        image.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) -> {
                openGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            else -> {
                readMediaImagePermissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }

    private fun showDatePicker() {
        return dataPicker.showDatePicker(
            parentFragmentManager,
            onPositiveButtonClickCallBack = { date ->
                dateEditText.setText(dataPicker.currentDay(Date(date)))
                createTransactionViewModel.setDate(date)
            }
        )
    }


    private fun onTextChangedListener(): TextWatcher {
        return object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                amountEditText.removeTextChangedListener(this)
                amountEditText.setText(s.toString())
                createTransactionViewModel.setAmount(s.toString().toDoubleOrNull())
                amountEditText.setSelection(amountEditText.text!!.length)
                amountEditText.addTextChangedListener(this)
            }
        }
    }
}
