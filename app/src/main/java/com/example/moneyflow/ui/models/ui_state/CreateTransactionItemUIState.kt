package com.example.moneyflow.ui.models.ui_state

import android.net.Uri
import com.example.moneyflow.domain.model.Category

data class CreateTransactionItemUIState(
    val amount: Double? = null,
    val category: Category? = null,
    val listOfCategory: List<String> = Category.values().map { it.name }.toList(),
    val date: Long = System.currentTimeMillis(),
    val image: Uri? = null,
)
