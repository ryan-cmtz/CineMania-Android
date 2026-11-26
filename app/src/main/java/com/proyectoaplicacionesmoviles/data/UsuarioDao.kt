package com.proyectoaplicacionesmoviles.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios ORDER BY nombre COLLATE NOCASE") fun observar(): Flow<List<UsuarioEntity>>
    @Insert suspend fun insertar(item: UsuarioEntity)
    @Update suspend fun actualizar(item: UsuarioEntity)
    @Delete suspend fun eliminar(item: UsuarioEntity)
}
