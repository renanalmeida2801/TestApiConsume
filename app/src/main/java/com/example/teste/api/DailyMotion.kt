package com.example.teste.api

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
        @Field("password") password: String
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
    )
}

data class AuthResponse(val access_token: String)
data class UploadUrlResponse(val upload_url: String)
