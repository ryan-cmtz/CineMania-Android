package com.proyectoaplicacionesmoviles.data.di

import com.proyectoaplicacionesmoviles.data.repository.ProductoRepositoryImp
import com.proyectoaplicacionesmoviles.data.repository.CatalogoRepositoryImp
import com.proyectoaplicacionesmoviles.domain.repository.CatalogoRepository
import com.proyectoaplicacionesmoviles.domain.repository.ProductoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductoRepository(implementation: ProductoRepositoryImp): ProductoRepository
    @Binds
    @Singleton
    abstract fun bindCatalogoRepository(implementation: CatalogoRepositoryImp): CatalogoRepository
}
