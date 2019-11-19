package com.leo.homeloan.main.helpers

import android.os.Bundle
import android.support.v4.app.Fragment
import java.util.*

open class BaseFragment<out T : BaseActivity> : Fragment() {
    companion object {

        /**
         * @param map pass null, if you want to create a new hash map, else pass the old hashmap, multiple params can be
         * achieved using it following way,
         * getMappedValues(key2, value2, getMappedValues( key1, value1, null ));
         */
        fun getMappedValues(key: String, value: ArgumentData, map: HashMap<String, ArgumentData>?): HashMap<String, ArgumentData> {
            var values = HashMap<String, ArgumentData>()
            if (map != null)
                values = map

            values[key] = value
            return values
        }
    }

    fun activity(): T = activity as T
    fun showLoader() = activity().showLoader()
    fun dismissLoader() = activity().dismissLoader()
    fun showErrorDialog(message: String, dismissListener: BaseActivity.DialogDismiss?) = activity().showErrorDialog(message,
            dismissListener)

    fun dismissErrorDialog() = activity().dismissErrorDialog()
    fun showToast(msg: String) = activity().showToast(msg)
    fun showMessageDialog(message: String, positiveButton: String, positiveListener: BaseActivity.DialogDismiss?, negativeButton: String, negativeListener: BaseActivity.DialogDismiss?) =
            activity()
                    .showMessageDialog(message, positiveButton, positiveListener, negativeButton, negativeListener)

    fun dismissMessageDialog() = activity().dismissMessageDialog()

    fun addValues(parameters: HashMap<String, ArgumentData>?) {
        if (parameters != null) {
            for ((key, argumentData) in parameters) {

                if (argumentData.type == String::class.java) {
                    addValue(key, argumentData.text)
                } else {
                    addValue(key, argumentData.int)
                }
            }
        }
    }

    fun addValue(key: String, value: String) {
        var args = arguments

        if (args == null)
            args = Bundle()

        args.putString(key, value)
        arguments = args
    }

    fun addValue(key: String, value: Int) {
        var args = arguments

        if (args == null)
            args = Bundle()

        args.putInt(key, value)
        arguments = args
    }

    fun getValue(key: String, defaultValue: Int): Int {
        val bundle = arguments
        try {
            return bundle!!.getInt(key, defaultValue)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    fun getValue(key: String, defaultValue: String): String {
        val bundle = arguments
        try {
            return bundle!!.getString(key, defaultValue)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    fun getInt(value: String): Int {
        try {
            return Integer.parseInt(value)
        } catch (e: Exception) {
            return 0
        }

    }

    fun requestPermission(code: Int, permission: String, callback: PermissionCallback) =
            activity().requestPermission(code, permission, callback)
}
