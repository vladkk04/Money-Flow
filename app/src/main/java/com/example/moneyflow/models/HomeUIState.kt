package com.example.moneyflow.models

import com.example.moneyflow.data.Transaction

data class HomeUIState(
    val transactionList: List<Transaction> = emptyList()
)
