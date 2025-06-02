package com.example.afbe.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val nif: String,
    val name: String,
    val surnames: String,
    val email: String,
    val password: String,
    val age: Int,
    val gender: String,
    val telephone: Int,
    val instrumento: @Contextual Instrumento,
    val rol: String,
    val userImage: String
)

