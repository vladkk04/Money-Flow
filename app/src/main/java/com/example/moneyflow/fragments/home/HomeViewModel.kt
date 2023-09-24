package com.example.moneyflow.fragments.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyflow.models.HomeUIState
import com.example.moneyflow.use_case.HomeUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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