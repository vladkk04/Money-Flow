package com.example.moneyflow.models

import com.example.moneyflow.utils.PermissionsMessageProvider

data class PermissionDialogUIState(
    val title: String,
    val message: PermissionsMessageProvider,
    val isPermanentlyDeclined: Boolean,
    val onOkClick: () -> Unit,
    val onDismissClick: () -> Unit,
    val onGoToAppSettingsClick: () -> Unit
)
