package com.cloudhired.api

import com.google.gson.annotations.SerializedName

data class ProfessionalSummary (
    @SerializedName("_id")
    val _id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("yoe")
    val yoe: String,
    @SerializedName("certifications")
    val certifications: String
)