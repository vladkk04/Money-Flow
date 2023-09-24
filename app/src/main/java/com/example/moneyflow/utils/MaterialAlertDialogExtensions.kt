package com.example.moneyflow.utils


import com.example.moneyflow.models.PermissionDialogUIState
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun MaterialAlertDialogBuilder.permissionDialogShow(permissionsDialogUIState: PermissionDialogUIState) {
    val buttonText = if (permissionsDialogUIState.isPermanentlyDeclined) {
        "Grant permission"
    } else {
        "Ok"
    }

    MaterialAlertDialogBuilder(this@permissionDialogShow.context)
        .setTitle(permissionsDialogUIState.title)
        .setMessage(permissionsDialogUIState.message.getMessage(permissionsDialogUIState.isPermanentlyDeclined))
        .setPositiveButton(buttonText) { _, _ ->
            permissionsDialogUIState.onAllowClick()
        }
        .setOnDismissListener {
            permissionsDialogUIState.onDismissClick()
        }.show()
}



class CameraPermissionMessageProvider: PermissionsMessageProvider {
    override fun getMessage(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined camera permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your camera so that your friends " +
                    "can see you in a call."
        }
    }
}

class GalleryPermissionMessageProvider: PermissionsMessageProvider {
    override fun getMessage(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined gallery access. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your gallery so that you can " +
                    "select photos to share with your friends."
        }
    }
}







