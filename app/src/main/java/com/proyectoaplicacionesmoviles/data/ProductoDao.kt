package com.proyectoaplicacionesmoviles.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY nombre COLLATE NOCASE") fun observarProductos(): Flow<List<ProductoEntity>>
    @Insert suspend fun insertar(producto: ProductoEntity)
    @Update suspend fun actualizar(producto: ProductoEntity)
    @Delete suspend fun eliminar(producto: ProductoEntity)
}
