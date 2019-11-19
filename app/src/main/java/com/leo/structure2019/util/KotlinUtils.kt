package com.leo.homeloan.util

import timber.log.Timber

inline fun safeExecute(tag: String = "", functionBody: () -> Unit){
    try {
        functionBody()
    }
    catch (exception : Throwable){
        if (tag.isNotEmpty())
            Timber.i(tag,"Exception while executing safeExecute",exception)
    }
}