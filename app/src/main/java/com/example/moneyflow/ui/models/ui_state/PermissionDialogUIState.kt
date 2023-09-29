package com.example.moneyflow.ui.models.ui_state

import com.example.moneyflow.domain.model.PermissionsMessageProvider

data class PermissionDialogUIState(
    val title: String,
    val message: PermissionsMessageProvider,
    val isPermanentlyDeclined: Boolean,
    val onOkClick: () -> Unit,
    val onDismissClick: () -> Unit,
    val onGoToAppSettingsClick: () -> Unit
)
