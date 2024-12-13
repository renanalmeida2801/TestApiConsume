package com.example.teste.api

import com.example.teste.model.TokenRequest
import com.example.teste.model.TokenResponse
import com.example.teste.model.UploadUrlResponse
import com.example.teste.model.VideoDetails
import com.example.teste.model.VideoInfo
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface ApiDailyMotion {
    @POST
    fun getAcessToken(@Body request: TokenRequest): Call<TokenResponse>

    @GET
    fun getUrl(@Header("Authorization") token:String):Call<UploadUrlResponse>

    @Multipart
    @POST
    fun uploadVideo(@Part file: MultipartBody.Part): Call<VideoInfo>

    @POST
    fun postVideoChannel(@Url url:String, urlVideo: String, @Header("Authorization") token:String): Call<VideoDetails>

    @POST
    fun modifyTittle(url: String, @Header("Authorization") token:String): Call<VideoDetails>


}