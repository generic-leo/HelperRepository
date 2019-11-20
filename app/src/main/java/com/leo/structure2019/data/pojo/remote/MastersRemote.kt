package com.leo.homeloan.data.pojo.remote

import com.google.gson.annotations.SerializedName

data class MastersRemote(val OccupationMaster: ArrayList<OccupationRemote>,
                         val ReligionMaster: ArrayList<ReligionRemote>,
                         val CompanyMaster: ArrayList<CompanyRemote>,
                         val ResidenceMaster: ArrayList<ResidenceRemote>,
                         val QualificationMaster: ArrayList<QualificationRemote>,
                         val CategoryMaster: ArrayList<CategoryRemote>,
                         val Country: ArrayList<CountryRemote>,
                         val Promotion: ArrayList<PromotionRemote>,
                         val version: String)

data class MastersState(val State: ArrayList<StateRemote>)
data class MastersCity(val City: ArrayList<CityRemote>)
data class MastersZip(val Zipcode: ArrayList<ZipRemote>)
data class MastersCityState(val CityState: CityStateRemote)

data class OccupationRemote(val Occupation_id: Int, val Occupation_name: String, val Occupation_desc: String)
data class ReligionRemote(val religion_id: Int, val religion_name: String, val religion_desc: String)
data class CompanyRemote(val company_id: Int, val industry_id: String, val industry_desc: String)
data class ResidenceRemote(val residence_status_id: Int, val residence_status_name : String, val residence_status_desc: String)
data class QualificationRemote(val qualification_id: Int,val Qualification_name: String, val Qualification_desc: String)
data class CategoryRemote(val category_id: Int,val category_name: String,val category_desc: String)
data class CountryRemote(val country_id: Int,val country_name: String)
data class StateRemote(val state_id: Int,val state_desc: String)
data class CityRemote(val city_id: Int,val city_desc: String)
data class ZipRemote(val zip_id: Int,val zip_desc: String)
data class CityStateRemote(val state_id: String,val city_id: String, val state_name: String, val city_name: String)
data class PromotionRemote(
        @SerializedName("PROMOTIONDESC")
        val promotion_desc: String,
        @SerializedName("PROMOTIONID")
        val promotion_id: String)