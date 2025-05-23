package com.example.afbe.user

data class User(
    val nif: String,
    val name: String,
    val surnames: String,
    val email: String,
    val password: String,
    val age: Int,
    val gender: String,
    val telephone: Int,
    val instrumento: Int,
    val rol: String
)

