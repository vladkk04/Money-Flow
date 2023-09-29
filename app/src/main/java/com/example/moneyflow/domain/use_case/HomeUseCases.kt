package com.example.moneyflow.domain.use_case

data class HomeUseCases(
    val getTransactionsUseCase: GetTransactionsUseCase,
    val deleteTransactionUseCase: DeleteTransactionUseCase
)