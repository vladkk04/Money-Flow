package com.example.moneyflow.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyflow.ui.ui_state.HomeUIState
import com.example.moneyflow.domain.use_case.HomeUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val homeUseCases: HomeUseCases): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getAllTransactions()
    }

    private fun getAllTransactions() {
        viewModelScope.launch {
            homeUseCases.getTransactionsUseCase().collect {
                _uiState.update { stateUi ->
                    stateUi.copy(
                        transactionList = it
                    )
                }
            }
        }
    }
}