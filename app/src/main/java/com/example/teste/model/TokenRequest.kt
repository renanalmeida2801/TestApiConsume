package com.example.teste.model

data class TokenRequest(
    val grant_type: String,
    val client_id: String,
    val client_secret: String,
    val username: String,
    val password: String,
    val scope: String
)
