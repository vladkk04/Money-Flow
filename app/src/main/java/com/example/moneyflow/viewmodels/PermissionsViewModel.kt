package com.example.moneyflow.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moneyflow.models.PermissionDialogUIState

class PermissionsViewModel: ViewModel() {
    val permissionDialogQueue = mutableListOf<String>()

    fun dismissDialog() {
        permissionDialogQueue.removeLast()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !permissionDialogQueue.contains(permission)) {
            permissionDialogQueue.add(permission)
        }
    }
}
