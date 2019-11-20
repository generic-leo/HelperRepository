package com.leo.homeloan.data.retro

import com.leo.homeloan.data.pojo.remote.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface FCApiService{

    @FormUrlEncoded
    @POST("Login")
    fun authenticate(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("MobileOTP")
    fun registerMobile(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("dashboard")
    fun dashboard(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("newLoanApplication")
    fun newLoanApplication(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("fetchLoanApplicationData")
    fun applicantData(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SavePersonalDetails")
    fun savePersonal(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SaveContactDetails")
    fun saveContact(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SaveEmploymentDetails")
    fun saveOccupation(@Field("data")data: String): Call<APIResponse>

    @Multipart
    @POST("DocumentUpload")
    fun saveDocument(@Part("data")data: RequestBody, @Part array: Array<MultipartBody.Part>): Call<APIResponse>

    @FormUrlEncoded
    @POST("SaveLeadDetails")
    fun createLead(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SaveLoanDetails")
    fun saveLoanDetail(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("FetchLeadDetails")
    fun fetchLeadDetails(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SubmitApplicantLoan")
    fun saveReviewStatus(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SaveLoanStatus")
    fun updateStatus(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Masters")
    fun masters(@Field("data")data: String): Call<MastersRemote>

    @FormUrlEncoded
    @POST("State")
    fun stateMaster(@Field("data")data: String): Call<MastersState>

    @FormUrlEncoded
    @POST("City")
    fun cityMaster(@Field("data")data: String): Call<MastersCity>

    @FormUrlEncoded
    @POST("Zipcode")
    fun zipcodeMaster(@Field("data")data: String): Call<MastersZip>

    @FormUrlEncoded
    @POST("ZipcodeCityState")
    fun zipcodeCityState(@Field("data")data: String): Call<MastersCityState>

    @FormUrlEncoded
    @POST("SubmitApplicantLoan")
    fun submitApplication(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("PerformanceReport")
    fun performanceReport(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("VerifyPANcard")
    fun verifyPanCard(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("VerifyADHARcard")
    fun verifyAdhaarCard(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("VerifyOTP")
    fun verifyOTP(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("MobileOTP")
    fun sendOTP(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("MPin")
    fun setMPIN(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SearchApplicant")
    fun searchApplicant(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SetContinueCustomerForm ")
    fun sendOTPToCustomer(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SetCustomerFormConformation ")
    fun verifyOTPSentToCustomer(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("SetCustomerFormConformation ")
    fun setCustomerfromConformation(@Field("data")data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("UpdateLeadDetails")
    fun UpdateLeadStatus(@Field("data") data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("EasyPaymentUrl")
    fun EasyPaymentUrl(@Field("data") data: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("logout")
    fun logout(@Field("data")data: String): Call<APIResponse>
}
