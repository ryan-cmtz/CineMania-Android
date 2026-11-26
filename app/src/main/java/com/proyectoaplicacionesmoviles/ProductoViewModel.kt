package com.proyectoaplicacionesmoviles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyectoaplicacionesmoviles.domain.model.Producto
import com.proyectoaplicacionesmoviles.domain.model.Genero
import com.proyectoaplicacionesmoviles.domain.model.Usuario
import com.proyectoaplicacionesmoviles.domain.model.Resena
import com.proyectoaplicacionesmoviles.domain.repository.CatalogoRepository
import com.proyectoaplicacionesmoviles.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repository: ProductoRepository,
    private val catalogo: CatalogoRepository
) : ViewModel() {
    val productos = repository.observarProductos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val generos = catalogo.generos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val usuarios = catalogo.usuarios().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val resenas = catalogo.resenas().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()
    fun limpiarMensaje() { _mensaje.value = null }
    fun guardar(nombre: String, descripcion: String, actual: Producto? = null) {
        val limpio = nombre.trim()
        if (limpio.isEmpty()) { _mensaje.value = "Escribe el nombre de la película."; return }
        if (limpio.length > 20) { _mensaje.value = "El nombre no puede superar 20 caracteres."; return }
        ejecutarOperacion(if (actual == null) "Película agregada." else "Película actualizada.") {
            repository.guardarProducto(Producto(actual?.id ?: 0, limpio, descripcion.trim()))
        }
    }
    fun eliminar(producto: Producto) = ejecutarOperacion("Película eliminada.") { repository.eliminarProducto(producto) }
    fun guardarGenero(nombre: String, id: Int = 0): Boolean {
        if (nombre.isBlank()) { _mensaje.value = "Escribe el nombre del género."; return false }
        ejecutarOperacion(if (id == 0) "Género agregado." else "Género actualizado.") { catalogo.guardarGenero(Genero(id, nombre)) }
        return true
    }
    fun eliminarGenero(item: Genero) = ejecutarOperacion("Género eliminado.") { catalogo.borrarGenero(item) }
    fun guardarUsuario(nombre: String, correo: String, id: Int = 0): Boolean {
        if (nombre.isBlank() || !correo.trim().matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))) { _mensaje.value = "Escribe un nombre y un correo válido."; return false }
        ejecutarOperacion(if (id == 0) "Usuario agregado." else "Usuario actualizado.") { catalogo.guardarUsuario(Usuario(id, nombre, correo)) }
        return true
    }
    fun eliminarUsuario(item: Usuario) = ejecutarOperacion("Usuario eliminado.") { catalogo.borrarUsuario(item) }
    fun guardarResena(pelicula: String, usuario: String, puntuacion: Int, comentario: String, id: Int = 0): Boolean {
        if (pelicula.isBlank() || usuario.isBlank() || puntuacion !in 1..5) { _mensaje.value = "Completa película, usuario y una puntuación de 1 a 5."; return false }
        ejecutarOperacion(if (id == 0) "Reseña agregada." else "Reseña actualizada.") { catalogo.guardarResena(Resena(id, pelicula, usuario, puntuacion, comentario)) }
        return true
    }
    fun eliminarResena(item: Resena) = ejecutarOperacion("Reseña eliminada.") { catalogo.borrarResena(item) }

    private fun ejecutarOperacion(mensajeExito: String, operacion: suspend () -> Unit) {
        viewModelScope.launch {
            _mensaje.value = null
            try {
                operacion()
                _mensaje.value = mensajeExito
            } catch (error: Exception) {
                _mensaje.value = "No se pudo completar la operación. Revisa los datos e inténtalo de nuevo."
            }
        }
    }
}
