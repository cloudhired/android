package com.cloudhired.model

import com.google.gson.annotations.SerializedName

data class Notification (
    @SerializedName("_id")
    val _id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("time")
    val time: String
)