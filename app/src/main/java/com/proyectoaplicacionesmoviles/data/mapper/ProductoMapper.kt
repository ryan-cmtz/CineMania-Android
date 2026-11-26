package com.proyectoaplicacionesmoviles.data.mapper

import com.proyectoaplicacionesmoviles.data.ProductoEntity
import com.proyectoaplicacionesmoviles.domain.model.Producto

fun ProductoEntity.toDomain() = Producto(id = id, nombre = nombre, descripcion = descripcion)
fun Producto.toEntity() = ProductoEntity(id = id, nombre = nombre, descripcion = descripcion)
