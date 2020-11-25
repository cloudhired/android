package com.cloudhired.api

import com.google.gson.annotations.SerializedName

data class ProfessionalSummary (
    @SerializedName("_id")
    val _id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("job_title")
    val job_title: String,
    @SerializedName("current_loc")
    val current_loc: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("number_certs")
    val number_certs: String
)