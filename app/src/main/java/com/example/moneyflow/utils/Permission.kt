package com.example.moneyflow.utils

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.askPermission(vararg permission: String, callbacks: PermissionCallbacks.() -> Unit) {


}

fun Fragment.handleResultPermission(vararg permission: String, isGranted: Boolean) {

}

fun Fragment.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this.requireContext(), permission) == PackageManager.PERMISSION_GRANTED
}