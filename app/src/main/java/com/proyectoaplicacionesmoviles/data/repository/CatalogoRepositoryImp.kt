package com.proyectoaplicacionesmoviles.data.repository

import com.proyectoaplicacionesmoviles.data.*
import com.proyectoaplicacionesmoviles.domain.model.*
import com.proyectoaplicacionesmoviles.domain.repository.CatalogoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CatalogoRepositoryImp @Inject constructor(
    private val generoDao: GeneroDao,
    private val usuarioDao: UsuarioDao,
    private val resenaDao: ResenaDao
) : CatalogoRepository {
    override fun generos(): Flow<List<Genero>> = generoDao.observar().map { rows -> rows.map { Genero(it.id, it.nombre) } }
    override suspend fun guardarGenero(item: Genero) {
        val row = GeneroEntity(item.id, item.nombre.trim())
        if (item.id == 0) generoDao.insertar(row) else generoDao.actualizar(row)
    }
    override suspend fun borrarGenero(item: Genero) = generoDao.eliminar(GeneroEntity(item.id, item.nombre))

    override fun usuarios(): Flow<List<Usuario>> = usuarioDao.observar().map { rows -> rows.map { Usuario(it.id, it.nombre, it.correo) } }
    override suspend fun guardarUsuario(item: Usuario) {
        val row = UsuarioEntity(item.id, item.nombre.trim(), item.correo.trim())
        if (item.id == 0) usuarioDao.insertar(row) else usuarioDao.actualizar(row)
    }
    override suspend fun borrarUsuario(item: Usuario) = usuarioDao.eliminar(UsuarioEntity(item.id, item.nombre, item.correo))

    override fun resenas(): Flow<List<Resena>> = resenaDao.observar().map { rows -> rows.map { Resena(it.id, it.pelicula, it.usuario, it.puntuacion, it.comentario) } }
    override suspend fun guardarResena(item: Resena) {
        val row = ResenaEntity(item.id, item.pelicula.trim(), item.usuario.trim(), item.puntuacion, item.comentario.trim())
        if (item.id == 0) resenaDao.insertar(row) else resenaDao.actualizar(row)
    }
    override suspend fun borrarResena(item: Resena) = resenaDao.eliminar(ResenaEntity(item.id, item.pelicula, item.usuario, item.puntuacion, item.comentario))
}
