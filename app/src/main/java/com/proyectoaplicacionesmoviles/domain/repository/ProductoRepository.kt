package com.proyectoaplicacionesmoviles.domain.repository

import com.proyectoaplicacionesmoviles.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun observarProductos(): Flow<List<Producto>>
    suspend fun guardarProducto(producto: Producto)
    suspend fun eliminarProducto(producto: Producto)
}
