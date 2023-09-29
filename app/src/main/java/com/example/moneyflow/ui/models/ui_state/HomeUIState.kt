package com.example.moneyflow.ui.models.ui_state

import com.example.moneyflow.data.local.entities.Transaction

data class HomeUIState(
    val transactionList: List<Transaction> = emptyList()
)
