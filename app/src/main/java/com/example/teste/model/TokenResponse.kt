package com.example.teste.model

data class TokenResponse(
    val acess_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: String,
    val uid: String,
    val email_verifier: Boolean
)

