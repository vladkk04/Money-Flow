package com.example.moneyflow.domain.use_case

import com.example.moneyflow.data.local.repository.TransactionRepository

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