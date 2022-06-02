package com.zaniva.storyappv2.story

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("error")
    val state: Boolean,

    @SerializedName("message")
    val result: String,

    @SerializedName("listStory")
    val list: List<Stories>
)

data class Stories(
    @SerializedName("photoUrl")
    val photo: String,

    @SerializedName("createdAt")
    val date: String,

    val description: String,
    val name: String,
    val lat: Double,
    val lon: Double
)

data class FileUploadResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
