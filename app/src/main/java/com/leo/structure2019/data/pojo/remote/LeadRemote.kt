package com.leo.homeloan.data.pojo.remote

data class LeadRemote(
        val leadStatus: String,
        val leadid: Int,
        val name: String,
        val mobno: String,
        val emailid: String,
        val pincode: String,
        val typeOfLoan: String,
        val loanAmountRequested: String,
        val type: String,
        val empcode: String,
        val status: String,
        val updatedDate: String)