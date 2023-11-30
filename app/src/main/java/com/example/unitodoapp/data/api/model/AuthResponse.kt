package com.example.unitodoapp.data.api.model

data class AuthResponse(
    val accessToken: String,
    val user: UserResponse
)
