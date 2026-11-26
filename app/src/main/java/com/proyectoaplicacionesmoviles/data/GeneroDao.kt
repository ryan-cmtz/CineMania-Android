package com.proyectoaplicacionesmoviles.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GeneroDao {
    @Query("SELECT * FROM generos ORDER BY nombre COLLATE NOCASE") fun observar(): Flow<List<GeneroEntity>>
    @Insert suspend fun insertar(item: GeneroEntity)
    @Update suspend fun actualizar(item: GeneroEntity)
    @Delete suspend fun eliminar(item: GeneroEntity)
}
