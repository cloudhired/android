package com.cloudhired.api

import com.google.gson.annotations.SerializedName

data class ProfessionalProfile (
    @SerializedName("_id")
    val _id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("fname")
    val fname: String,
    @SerializedName("lname")
    val lname: String,
    @SerializedName("job_title")
    val job_title: String,
    @SerializedName("current_loc")
    val current_loc: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("yoe")
    val yoe: String,
    @SerializedName("personal_site")
    val personal_site: String,
    @SerializedName("linkedin_handle")
    val linkedin_handle: String,
    @SerializedName("github_handle")
    val github_handle: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("intro")
    val intro: String,
    @SerializedName("skills")
    val skills: List<String>,
    @SerializedName("certs")
    val certs: List<Cert>,
    @SerializedName("courses")
    val courses: List<Course>,

    @SerializedName("number_certs")
    val number_certs: String
)

data class Cert (
    @SerializedName("cert_name")
    val cert_name: String,
    @SerializedName("date_issued")
    val date_issued: String,
    @SerializedName("date_expired")
    val date_expired: String,
    @SerializedName("verify_link")
    val verify_link: String
)

data class Course (
    @SerializedName("name")
    val name: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("date_finished")
    val date_finished: String,
    @SerializedName("outcome")
    val outcome: String
)