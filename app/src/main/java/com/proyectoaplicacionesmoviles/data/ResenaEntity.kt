package com.proyectoaplicacionesmoviles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resenas")
data class ResenaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pelicula: String,
    val usuario: String,
    val puntuacion: Int,
    val comentario: String
)
