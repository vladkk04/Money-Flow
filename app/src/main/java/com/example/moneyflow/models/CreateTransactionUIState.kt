package com.example.moneyflow.models

data class CreateTransactionUIState(
    val isCreationAllowed: Boolean = false,
    val isCreated: Boolean = false,
    val isActiveDataPicker: Boolean = false,
    val showGallery: () -> Unit,
    val showDatePicker: () -> Unit
)
