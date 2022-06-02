package com.zaniva.storyappv2.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val state: Boolean,

    @SerializedName("loginResult")
    val result: Session
)
