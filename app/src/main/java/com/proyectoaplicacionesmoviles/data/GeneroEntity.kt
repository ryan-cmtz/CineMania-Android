package com.proyectoaplicacionesmoviles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generos")
data class GeneroEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0, val nombre: String)
