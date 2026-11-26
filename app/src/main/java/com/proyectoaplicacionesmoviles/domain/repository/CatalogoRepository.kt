package com.proyectoaplicacionesmoviles.domain.repository

import com.proyectoaplicacionesmoviles.domain.model.*
import kotlinx.coroutines.flow.Flow

interface CatalogoRepository {
    fun generos(): Flow<List<Genero>>
    suspend fun guardarGenero(item: Genero)
    suspend fun borrarGenero(item: Genero)
    fun usuarios(): Flow<List<Usuario>>
    suspend fun guardarUsuario(item: Usuario)
    suspend fun borrarUsuario(item: Usuario)
    fun resenas(): Flow<List<Resena>>
    suspend fun guardarResena(item: Resena)
    suspend fun borrarResena(item: Resena)
}
