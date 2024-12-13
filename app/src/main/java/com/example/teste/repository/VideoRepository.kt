package com.example.teste.repository

import com.example.teste.api.DailyMotion
import okhttp3.MultipartBody

class VideoRepository(private val api: DailyMotion){
    suspend fun getUploadUrl(token:String) = api.getUploadUrl("Bearer $token")
    suspend fun uploadVideo(url:String, video:MultipartBody.Part)=api.uploadVideo(url,video)
}
