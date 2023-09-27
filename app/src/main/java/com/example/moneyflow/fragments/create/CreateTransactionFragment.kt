package com.example.moneyflow.fragments.create

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.moneyflow.fragments.base.BaseScreen
import com.example.moneyflow.models.CreateTransactionItemUIState
import com.example.moneyflow.models.PermissionDialogUIState
import com.example.moneyflow.utils.CameraPermissionMessageProvider
import com.example.moneyflow.utils.Category
import com.example.moneyflow.utils.DataPickerUtils
import com.example.moneyflow.utils.GalleryPermissionMessageProvider
import com.example.moneyflow.utils.permissionDialogShow
import com.example.moneyflow.viewmodels.PermissionsViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    private var isAllowedOpenGallery: Boolean = false

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateNewTransactionBinding.inflate(inflater, container, false)

        amountEditText.addTextChangedListener(onTextChangedListener())

        setOnClickListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI(createTransactionViewModel.uiItemState.value)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeCreateTransactionUIState()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private suspend fun observeCreateTransactionUIState() {
        createTransactionViewModel.uiState.collectLatest { uiState ->
            if(uiState.isCreated)
                updateUI(createTransactionViewModel.uiItemState.value)
        }
    }

    private val openGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {

    }

    private val readMediaImagePermissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        permissionsViewModel.onPermissionResult(Manifest.permission.READ_MEDIA_IMAGES, isGranted)

        permissionsViewModel.permissionDialogQueue.reversed().forEach {
            showReadMediaImagePermissionDialog(it)
        }
    }

    private fun showReadMediaImagePermissionDialog(permission: String) {
        val permissionDialogUIState = PermissionDialogUIState(
            title = "Hello",
            message = when (permission) {
                Manifest.permission.READ_MEDIA_IMAGES -> {
                    GalleryPermissionMessageProvider()
                }
                else -> {CameraPermissionMessageProvider()}
            },
            isPermanentlyDeclined = shouldShowRequestPermissionRationale(permission),
            onDismissClick = permissionsViewModel::dismissDialog,
            onOkClick = {
                permissionsViewModel::dismissDialog
                readMediaImagePermissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            },
            onGoToAppSettingsClick = ::goToAppSettings
        )

        MaterialAlertDialogBuilder(requireContext()).permissionDialogShow(permissionDialogUIState)
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
        dateEditText.setText(DataPickerUtils.currentDay(Date(uiState.date)))
    }

    private fun setOnClickListeners() {
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
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            else -> {
                readMediaImagePermissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select dates")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build().also {
                it.addOnPositiveButtonClickListener { date ->
                    dateEditText.setText(DataPickerUtils.currentDay(Date(date)))
                    createTransactionViewModel.setDate(date)
                }
                return it.show(requireActivity().supportFragmentManager, "data-picker")
            }
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
