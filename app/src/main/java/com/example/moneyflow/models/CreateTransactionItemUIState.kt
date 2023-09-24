package com.example.moneyflow.models

import com.example.moneyflow.utils.Category

data class CreateTransactionItemUIState(
    val amount: Double? = null,
    val category: Category? = null,
    val listOfCategory: List<String> = Category.values().map { it.name }.toList(),
    val date: Long = System.currentTimeMillis(),
    val image: Int? = null,
)
