package com.example.teste.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.dailymotion.com/"

    val instance: ApiDailyMotion by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Para converter JSON
            .build()
            .create(ApiDailyMotion::class.java)
    }
}