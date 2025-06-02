package com.example.afbe.publicaciones

data class Publicacion(
    val id: Long,
    val imagen: String,
    val titulo: String,
    val descripcion: String,
    val usuario: String
)
