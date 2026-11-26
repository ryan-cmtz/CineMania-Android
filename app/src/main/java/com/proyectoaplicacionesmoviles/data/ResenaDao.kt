package com.proyectoaplicacionesmoviles.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ResenaDao {
    @Query("SELECT * FROM resenas ORDER BY pelicula COLLATE NOCASE") fun observar(): Flow<List<ResenaEntity>>
    @Insert suspend fun insertar(item: ResenaEntity)
    @Update suspend fun actualizar(item: ResenaEntity)
    @Delete suspend fun eliminar(item: ResenaEntity)
}
