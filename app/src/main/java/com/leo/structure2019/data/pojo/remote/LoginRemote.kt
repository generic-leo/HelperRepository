package com.leo.homeloan.data.pojo.remote

data class LoginRemote(val userid: String,
                       val mobno: String,
                       val otp: String,
                       val mpin: String,
                       val updatedDate: String)