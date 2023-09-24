package com.example.moneyflow.use_case

data class HomeUseCases(
    val getTransactionsUseCase: GetTransactionsUseCase,
    val deleteTransactionUseCase: DeleteTransactionUseCase
)