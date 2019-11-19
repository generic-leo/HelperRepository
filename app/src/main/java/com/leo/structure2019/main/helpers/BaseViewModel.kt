package com.leo.homeloan.main.helpers

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

open class BaseViewModel(appContext: Application): AndroidViewModel(appContext){

    // region [ Delegation of Authority ]
    private val commandDelegate = NotifyLiveEvent()

    fun notifyEvent(event: NotifyEvent){
        commandDelegate.value = event
    }

    fun events() = commandDelegate
    // endregion
}
