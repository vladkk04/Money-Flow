package com.example.moneyflow.utils


import com.example.moneyflow.domain.model.PermissionsMessageProvider
import com.example.moneyflow.ui.models.ui_state.PermissionDialogUIState
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun MaterialAlertDialogBuilder.showPermissionDialog(permissionsDialogUIState: PermissionDialogUIState) {
    val positiveButtonText = if (!permissionsDialogUIState.isPermanentlyDeclined) {
        "Grant permission"
    } else {
        "Ok"
    }

    MaterialAlertDialogBuilder(this@showPermissionDialog.context)
        .setTitle(permissionsDialogUIState.title)
        .setMessage(permissionsDialogUIState.message.getMessage(permissionsDialogUIState.isPermanentlyDeclined))
        .setPositiveButton(positiveButtonText) { _, _ ->
            if (permissionsDialogUIState.isPermanentlyDeclined) {
                permissionsDialogUIState.onOkClick()
            } else {
                permissionsDialogUIState.onGoToAppSettingsClick()
            }
        }
        .setOnDismissListener {
            permissionsDialogUIState.onDismissClick()
        }.show()
}

class ReadImageAndVideoPermissionMessageProvider: PermissionsMessageProvider {
    override fun getMessage(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined gallery access. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your gallery so that you can "
        }
    }
}







