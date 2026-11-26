package com.proyectoaplicacionesmoviles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String = ""
) {
    init {
        require(nombre.length <= 20) { "El nombre no puede tener más de 20 caracteres" }
    }
}
