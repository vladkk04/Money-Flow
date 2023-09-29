package com.example.moneyflow.ui.viewmodels

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneyflow.ui.MainActivity
import com.example.moneyflow.data.di.AppModule
import com.example.moneyflow.domain.use_case.TransactionUseCases
import com.example.moneyflow.ui.fragments.create.CreateTransactionViewModel

class CreateTransactionViewModelFactory(
    private val transactionUseCase: TransactionUseCases
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateTransactionViewModel(transactionUseCase.createTransactionUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

inline fun <reified VM : ViewModel> MainActivity.createTransactionViewModel() = viewModels<VM> {
    CreateTransactionViewModelFactory(AppModule.transactionUseCase)
}

