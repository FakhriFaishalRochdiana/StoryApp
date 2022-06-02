package com.zaniva.storyappv2.data

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("error")
    val state: Boolean,

    @SerializedName("message")
    val result: String

)
