package com.example.teste.model

data class VideoDetails(
    val id: String,
    val title: String,
    val channel: String?, // Pode ser null
    val owner: String
)
