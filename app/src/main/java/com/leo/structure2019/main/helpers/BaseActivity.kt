package com.leo.homeloan.main.helpers

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.leo.homeloan.R
import com.leo.homeloan.util.safeExecute
import timber.log.Timber

open class BaseActivity : AppCompatActivity(){

    private var messageDialog: Dialog? = null

    private var isShowingLoader = false
    private var loadingDialog: Dialog? = null

    fun showLoader() {
        if(isShowingLoader)
            return

        dismissLoader()

        loadingDialog = Dialog(this)
        loadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog?.setCancelable(false)
        loadingDialog?.setContentView(R.layout.dialog_connecting)
        loadingDialog?.show()

        (loadingDialog as Dialog).findViewById<TextView>(R.id.message).text = "Loading.."

        isShowingLoader = true
        Timber.i("show loader dialog")
    }

    fun dismissLoader() {
        if(!isShowingLoader)
            return

        isShowingLoader = false
        safeExecute {
            loadingDialog?.dismiss()
        }
        Timber.i("hide loader dialog")
    }

    fun showErrorDialog(message: String, dismissListener: DialogDismiss?) {
        dismissErrorDialog()
        showMessageDialog(message, "OK", dismissListener)
    }

    fun dismissErrorDialog() {
        dismissMessageDialog()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showMessageDialog(message: String, positiveButton: String="", positiveListener: DialogDismiss? = null, negativeButton: String="", negativeListener: DialogDismiss? = null) {
        dismissMessageDialog()

        messageDialog = Dialog(this)
        messageDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        messageDialog?.setCancelable(false)
        messageDialog?.setContentView(R.layout.dialog_alert)
        messageDialog?.show()

        messageDialog?.findViewById<TextView>(R.id.message)?.text = message

        if (positiveButton.isNotBlank())
            messageDialog?.findViewById<TextView>(R.id.confirm)?.text = positiveButton
        else
            messageDialog?.findViewById<TextView>(R.id.confirm)?.visibility = View.GONE

        messageDialog?.findViewById<TextView>(R.id.confirm)?.setOnClickListener{
            positiveListener?.onDismiss()
            dismissMessageDialog()
        }

        if (negativeButton.isNotBlank())
            messageDialog?.findViewById<TextView>(R.id.cancel)?.text = negativeButton
        else
            messageDialog?.findViewById<TextView>(R.id.cancel)?.visibility = View.GONE

        messageDialog?.findViewById<TextView>(R.id.cancel)?.setOnClickListener{
            negativeListener?.onDismiss()
            dismissMessageDialog()
        }
    }

    fun dismissMessageDialog() {
        safeExecute {
            messageDialog?.dismiss()
        }
    }

    interface DialogDismiss{
        fun onDismiss()
    }

    // region [ Runtime Permission ]
    private var callbackPermissionResult: PermissionCallback? = null
    private var callbackRequestCode: Int = 0

    fun requestPermission(requestCode: Int, permission: String, callback: PermissionCallback) {
        callbackPermissionResult = callback
        callbackRequestCode = requestCode

        val permissions = getPermissionArray(permission)
        if(checkPermission(permissions)){
            callbackPermissionResult?.permissionGranted(requestCode)
        }
        else{
            if (showRationale(permissions)){
                requestPermission(permissions, requestCode)

                // If You Don't Want To Show Message
                // callbackPermissionResult?.permissionDenied(getPermissionErrorMessage())
            }
            else{
                // Permission Not Granted
                requestPermission(permissions, requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            callbackRequestCode -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Final Check ; All Permission
                    if(checkPermission(permissions)){
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        callbackPermissionResult?.permissionGranted(requestCode)
                    }else{
                        callbackPermissionResult?.permissionDenied(requestCode,getPermissionErrorMessage())
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    callbackPermissionResult?.permissionDenied(requestCode,getPermissionErrorMessage())
                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun checkPermission(permissions: Array<String>): Boolean{
        for (permissionName in permissions){
            if (ContextCompat.checkSelfPermission(applicationContext, permissionName)
                    == PackageManager.PERMISSION_DENIED)
                return false
        }

        return true
    }

    private fun showRationale(permissions: Array<String>): Boolean{
        for (permissionName in permissions){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permissionName)){
                return true
            }
        }

        return false
    }

    private fun requestPermission(permissions: Array<String>, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
    }

    private fun getPermissionArray(permission: String): Array<String> {
        when(permission){
            "camera" -> return arrayOf(Manifest.permission.CAMERA)
            "storage" -> return arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            "camera_storage" -> return arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            "imei" -> return arrayOf(Manifest.permission.READ_PHONE_STATE)
            "call" -> return arrayOf(Manifest.permission.CALL_PHONE)
        }

        return arrayOf()
    }

    private fun getPermissionErrorMessage(): String = "Permission not granted. You need to allow access to all permissions"

    // endregion
}
