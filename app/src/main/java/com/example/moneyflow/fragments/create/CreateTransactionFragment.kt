package com.example.moneyflow.fragments.create

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moneyflow.R
import com.example.moneyflow.databinding.FragmentCreateNewTransactionBinding
import com.example.moneyflow.fragments.base.BaseScreen
import com.example.moneyflow.models.CreateTransactionItemUIState
import com.example.moneyflow.utils.Category
import com.example.moneyflow.utils.DataPickerUtils
import com.example.moneyflow.viewmodels.PermissionsViewModel
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

        amountEditText.addTextChangedListener(onTextChangedListener())

        dateEditText.setOnClickListener {
            createTransactionViewModel.uiState.value.showDatePicker()
        }

        image.setOnClickListener {
            createTransactionViewModel.uiState.value.showGallery()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAutoComplete.setOnItemClickListener { _, _, _, id ->
            createTransactionViewModel.setCategory(Category.values()[id.toInt()])
            categoryInputText.setStartIconDrawable(createTransactionViewModel.uiItemState.value.category!!.icon)
        }

        categoryAutoComplete.setSimpleItems(createTransactionViewModel.uiItemState.value.listOfCategory.toTypedArray())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                updateUI(createTransactionViewModel.uiItemState.value)

                launch {
                    createTransactionViewModel.uiState.collectLatest { uiState ->
                        if(uiState.isCreated)
                            updateUI(createTransactionViewModel.uiItemState.value)
                        if(uiState.isActiveDataPicker)
                            showDataPicker()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*@RequiresApi(Build.VERSION_CODES.P)
    private var registerOnResult = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri)

            val listener = ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
                    decoder.setTargetSampleSize(4)
            }

            val bitmap = ImageDecoder.decodeBitmap(source, listener)

            ImageSaver().saveToInternalStorage(requireContext(), bitmap)

            Log.d("d", ImageSaver().saveToInternalStorage(requireContext(), bitmap)!!)
        }
    }*/

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.keys.forEach { permission ->
            permissionsViewModel.onPermissionResult(permission, permissions[permission] == true)
        }
    }

    private fun showDataPicker() {
        DataPickerUtils.datePicker.addOnPositiveButtonClickListener {
            dateEditText.setText(DataPickerUtils.currentDay(Date(it)))
            createTransactionViewModel.setDate(it)
        }
        DataPickerUtils.datePicker.show(requireActivity().supportFragmentManager, "data-picker")
    }

    private fun updateUI(uiState: CreateTransactionItemUIState) {
        amountEditText.setText(uiState.amount?.toString())
        categoryAutoComplete.setText(uiState.category?.name)
        categoryInputText.setStartIconDrawable(uiState.category?.icon ?: R.drawable.ic_category)
        dateEditText.setText(DataPickerUtils.currentDay(Date(uiState.date)))
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
