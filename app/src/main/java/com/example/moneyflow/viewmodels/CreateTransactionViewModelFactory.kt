package com.example.moneyflow.viewmodels

import android.app.Application
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneyflow.activities.MainActivity
import com.example.moneyflow.di.AppModule
import com.example.moneyflow.di.MoneyFlowApplication
import com.example.moneyflow.di.TransactionUseCases
import com.example.moneyflow.fragments.create.CreateTransactionViewModel

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

