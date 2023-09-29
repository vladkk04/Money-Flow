package com.example.moneyflow.ui.viewmodels

import androidx.lifecycle.ViewModel

class PermissionsViewModel: ViewModel() {
    val permissionDialogQueue = mutableListOf<String>()

    fun dismissDialog() {
        permissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !permissionDialogQueue.contains(permission)) {
            permissionDialogQueue.add(permission)
        }
    }
}
