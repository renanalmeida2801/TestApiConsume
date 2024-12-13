package com.example.teste.api

import com.example.teste.model.AuthResponse
import com.example.teste.model.UploadUrlResponse
import com.example.teste.model.VideoDetails
import okhttp3.MultipartBody
import retrofit2.http.*

interface DailyMotion {
    @POST("oauth/token")
    @FormUrlEncoded
    suspend fun authenticate(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String
    ): AuthResponse

    @GET("file/upload")
    suspend fun getUploadUrl(
        @Header("Authorization") authorization: String
    ): UploadUrlResponse

    @POST
    suspend fun uploadVideo(
        @Url uploadUrl: String,
        @Body file: MultipartBody.Part
    )

    @POST("user/me/videos")
    @FormUrlEncoded
    suspend fun createVideo(
        @Header("Authorization") authorization: String,
        @FieldMap videoDetails: Map<String, String>
    ): VideoDetails
}

