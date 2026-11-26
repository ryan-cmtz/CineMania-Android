package com.proyectoaplicacionesmoviles.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS generos (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL)")
            database.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL, correo TEXT NOT NULL)")
            database.execSQL("CREATE TABLE IF NOT EXISTS resenas (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, pelicula TEXT NOT NULL, usuario TEXT NOT NULL, puntuacion INTEGER NOT NULL, comentario TEXT NOT NULL)")
        }
    }
    @Provides @Singleton fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "cinemania.db")
            .addMigrations(MIGRATION_1_2).build()
    @Provides fun provideProductoDao(database: AppDatabase): ProductoDao = database.productoDao()
    @Provides fun provideGeneroDao(database: AppDatabase): GeneroDao = database.generoDao()
    @Provides fun provideUsuarioDao(database: AppDatabase): UsuarioDao = database.usuarioDao()
    @Provides fun provideResenaDao(database: AppDatabase): ResenaDao = database.resenaDao()
}
