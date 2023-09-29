package com.example.moneyflow.ui.fragments.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyflow.ui.ui_state.CreateTransactionItemUIState
import com.example.moneyflow.ui.ui_state.CreateTransactionUIState
import com.example.moneyflow.domain.use_case.CreateTransactionUseCase
import com.example.moneyflow.domain.model.Category
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTransactionViewModel(
    private val createTransactionUseCase: CreateTransactionUseCase
): ViewModel() {

    private val _uiItemState = MutableStateFlow(CreateTransactionItemUIState())
    val uiItemState get() = _uiItemState.asStateFlow()

    private val _uiState = MutableStateFlow(CreateTransactionUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiItemState.collect {
                checkValues(it.amount, it.category)
            }
        }
    }


    fun setAmount(amount: Double?) {
        if (amount != null) {
            _uiItemState.update {
                it.copy(amount = amount)
            }
        }
    }

    fun setCategory(category: Category?) {
        if (category != null) {
            _uiItemState.update {
                it.copy(category = category)
            }
        }
    }

    fun setDate(date: Long?) {
        if (date != null) {
            _uiItemState.update {
                it.copy(date = date)
            }
        }
    }

    fun setImage(image: Uri?) {
        if (image != null) {
            _uiItemState.update {
                it.copy(
                    image = image
                )
            }
        }
    }

    fun createNewTransaction() {
        viewModelScope.launch {
            try {
                createTransactionUseCase(
                    _uiItemState.value.amount,
                    _uiItemState.value.category,
                    _uiItemState.value.date,
                    _uiItemState.value.image
                )

                resetValues()

                _uiState.update {
                    it.copy(
                        isCreationAllowed = false,
                        isCreated = true
                    )
                }
            } catch(cancellationException: CancellationException) {
                throw cancellationException
            }
        }
    }

    private fun resetValues() {
        _uiItemState.update {
            it.copy(
                amount = null,
                category = null,
                date = System.currentTimeMillis(),
                image = null,
            )
        }
    }

    private fun checkValues(amount: Double?, category: Category?) {
        if (amount != null && category != null) {
            _uiState.update {
                it.copy(
                    isCreationAllowed = true,
                    isCreated = false
                )
            }
        }
    }
}
