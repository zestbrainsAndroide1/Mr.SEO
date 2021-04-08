package com.zb.mindme.permission

interface PermissionStatus {
    fun allGranted()
    fun onDenied()
    fun foreverDenied()
}