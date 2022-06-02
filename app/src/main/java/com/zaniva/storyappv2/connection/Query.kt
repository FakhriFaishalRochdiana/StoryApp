package com.zaniva.storyappv2.connection

import com.zaniva.storyappv2.data.LoginResponse
import com.zaniva.storyappv2.data.RegisterResponse
import com.zaniva.storyappv2.story.FileUploadResponse
import com.zaniva.storyappv2.story.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface Query {

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun regisUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    fun listStory(
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part("description") description: String,
        @Part file: MultipartBody.Part
    ): Call<FileUploadResponse>

}