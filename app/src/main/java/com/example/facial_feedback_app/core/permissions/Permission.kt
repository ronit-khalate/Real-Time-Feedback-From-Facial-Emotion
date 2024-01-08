package com.example.facial_feedback_app.core.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionManager {

    val CAMAERAX_PERMISSIONS = Manifest.permission.CAMERA


    fun hasPermission(context:Context):Boolean{

        return ContextCompat.checkSelfPermission(
                context,
                CAMAERAX_PERMISSIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}