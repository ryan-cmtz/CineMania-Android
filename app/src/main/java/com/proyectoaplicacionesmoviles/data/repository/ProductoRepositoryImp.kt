package com.proyectoaplicacionesmoviles.data.repository

import com.proyectoaplicacionesmoviles.data.ProductoDao
import com.proyectoaplicacionesmoviles.data.mapper.toDomain
import com.proyectoaplicacionesmoviles.data.mapper.toEntity
import com.proyectoaplicacionesmoviles.domain.model.Producto
import com.proyectoaplicacionesmoviles.domain.repository.ProductoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductoRepositoryImp @Inject constructor(private val dao: ProductoDao) : ProductoRepository {
    override fun observarProductos(): Flow<List<Producto>> =
        dao.observarProductos().map { productos -> productos.map { it.toDomain() } }

    override suspend fun guardarProducto(producto: Producto) {
        val entity = producto.toEntity()
        if (producto.id == 0) dao.insertar(entity) else dao.actualizar(entity)
    }

    override suspend fun eliminarProducto(producto: Producto) = dao.eliminar(producto.toEntity())
}
