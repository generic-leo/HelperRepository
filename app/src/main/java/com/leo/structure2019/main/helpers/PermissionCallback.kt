package com.leo.homeloan.main.helpers

interface PermissionCallback{
    fun permissionGranted(requestCode: Int)
    fun permissionDenied(requestCode: Int, message: String)
}