package com.example.moneyflow.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moneyflow.models.PermissionDialogUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
