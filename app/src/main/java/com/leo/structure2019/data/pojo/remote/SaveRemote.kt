package com.leo.homeloan.data.pojo.remote

import timber.log.Timber

public data class SaveRemote (
    val loanid: String = "")
{
    fun parseLoanID(): Int {
        try {
            return Integer.parseInt(loanid)
        }catch (e : Exception){
            Timber.e(e)
        }

        return 0
    }
}
