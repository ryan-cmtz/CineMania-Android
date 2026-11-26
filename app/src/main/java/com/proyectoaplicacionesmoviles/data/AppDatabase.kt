package com.proyectoaplicacionesmoviles.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductoEntity::class, GeneroEntity::class, UsuarioEntity::class, ResenaEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun generoDao(): GeneroDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun resenaDao(): ResenaDao
}
