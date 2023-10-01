package com.example.moneyflow.ui.fragments.create

import android.Manifest
import android.content.Intent
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
import com.example.moneyflow.utils.ReadImageAndVideoPermissionMessageProvider
import com.example.moneyflow.utils.showPermissionDialog
import com.example.moneyflow.ui.viewmodels.PermissionsViewModel
import com.example.moneyflow.utils.currentDay
import com.example.moneyflow.utils.isPermissionGranted
import com.example.moneyflow.utils.showDatePicker
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

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateNewTransactionBinding.inflate(inflater, container, false)

        setupAllListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI(createTransactionViewModel.uiItemState.value)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                createTransactionViewModel.uiState.collectLatest {
                    if (it.isCreated) {
                        setupUI(createTransactionViewModel.uiItemState.value)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val pickVisualMediaResultLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            createTransactionViewModel.setImage(it)
            image.setImageURI(it)
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

    private fun openGallery() {
        if (isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)) {
            pickVisualMediaResultLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            readMediaImagePermissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }


    private fun goToAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        ).also(::startActivity)
    }

    private fun setupUI(uiState: CreateTransactionItemUIState) {
        amountEditText.setText(uiState.amount?.toString())
        categoryAutoComplete.setText(uiState.category?.name)
        categoryAutoComplete.setSimpleItems(uiState.listOfCategory.toTypedArray())
        categoryInputText.setStartIconDrawable(uiState.category?.icon ?: R.drawable.ic_category)
        dateEditText.setText(currentDay(Date(uiState.date)))
        image.setImageURI(uiState.image)
    }

    private fun setupAllListeners() {
        amountEditText.addTextChangedListener(onTextChangedListener())

        categoryAutoComplete.setOnItemClickListener { _, _, _, id ->
            createTransactionViewModel.setCategory(Category.values()[id.toInt()])
            categoryInputText.setStartIconDrawable(createTransactionViewModel.uiItemState.value.category!!.icon)
        }

        dateEditText.setOnClickListener {
            showDatePicker {
                createTransactionViewModel.setDate(it)
                dateEditText.setText(currentDay(Date(it)))
            }
        }

        image.setOnClickListener {
            openGallery()
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
