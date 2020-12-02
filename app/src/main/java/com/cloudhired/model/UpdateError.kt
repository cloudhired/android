package com.cloudhired.model

import com.google.gson.annotations.SerializedName

data class UpdateError (
    @SerializedName("error")
    val error: String?
)