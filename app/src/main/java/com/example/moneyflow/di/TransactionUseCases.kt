package com.example.moneyflow.di

import com.example.moneyflow.data.repository.TransactionRepository
import com.example.moneyflow.use_case.CreateTransactionUseCase
import com.example.moneyflow.use_case.DeleteTransactionUseCase
import com.example.moneyflow.use_case.GetTransactionsUseCase

class TransactionUseCases private constructor(
    val createTransactionUseCase: CreateTransactionUseCase,
    val getTransactionsUseCase: GetTransactionsUseCase,
    val deleteTransactionUseCases: DeleteTransactionUseCase,
) {
    companion object {
        private var instance: TransactionUseCases? = null

        fun getInstance(repository: TransactionRepository): TransactionUseCases {
            return instance ?: synchronized(this) {
                instance ?: TransactionUseCases(
                    CreateTransactionUseCase(repository),
                    GetTransactionsUseCase(repository),
                    DeleteTransactionUseCase(repository),
                ).also { instance = it }
            }
        }
    }
}