package com.leo.homeloan.data.pojo.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.reactivex.annotations.NonNull

@Entity(tableName = "occupation")
data class Occupation(@PrimaryKey
                      @NonNull
                      val Occupation_id: Int,
                      val Occupation_name: String,
                      val Occupation_desc: String){
    override fun toString() = Occupation_desc
}

@Entity(tableName = "religion")
data class Religion(@PrimaryKey
                    @NonNull
                    val religion_id: Int,
                    val religion_name: String,
                    val religion_desc: String){
    override fun toString(): String = religion_desc
}

@Entity(tableName = "company")
data class Company( @PrimaryKey
                    @NonNull
                    val company_id: Int,
                    val industry_id: String,
                    val industry_desc: String){
    override fun toString() = industry_desc
}

@Entity(tableName = "residence")
data class Residence(@PrimaryKey
                     @NonNull
                     val residence_status_id: Int,
                     val residence_status_name : String,
                     val residence_status_desc: String){
    override fun toString(): String = residence_status_desc
}

@Entity(tableName = "qualification")
data class Qualification(@PrimaryKey
                         @NonNull
                         val qualification_id: Int,
                         val Qualification_name: String,
                         val Qualification_desc: String){
    override fun toString() = Qualification_desc
}

@Entity(tableName = "category")
data class Category(@PrimaryKey
                    @NonNull
                    val category_id: Int,
                    val category_name: String,
                    val category_desc: String){
    override fun toString() = category_desc
}

@Entity(tableName = "country")
data class Country(@PrimaryKey
                    @NonNull
                    val country_id: Int,
                    val country_name: String){
    override fun toString() = country_name
}
data class State(
        @PrimaryKey
        @NonNull
        val state_id: Int,val state_desc: String){
    override fun toString() = state_desc
}
data class City(
        @PrimaryKey
        @NonNull
        val city_id: Int,val city_desc: String){
    override fun toString() = city_desc
}
data class Zip(
        @PrimaryKey
        @NonNull
        val zip_id: Int,val zip_desc: String){
    override fun toString() = zip_desc
}

@Entity(tableName = "promotion")
data class Promotion(
        @PrimaryKey
        @NonNull
        val promotion_id: String,
        val promotion_desc: String){
    override fun toString()= promotion_desc
}
