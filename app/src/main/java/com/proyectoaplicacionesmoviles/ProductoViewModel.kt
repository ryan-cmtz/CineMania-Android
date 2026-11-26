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

data class MensajeUi(val texto: String, val esError: Boolean)

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repository: ProductoRepository,
    private val catalogo: CatalogoRepository
) : ViewModel() {
    val productos = repository.observarProductos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val generos = catalogo.generos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val usuarios = catalogo.usuarios().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val resenas = catalogo.resenas().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val _mensaje = MutableStateFlow<MensajeUi?>(null)
    val mensaje = _mensaje.asStateFlow()
    fun limpiarMensaje() { _mensaje.value = null }
    fun guardar(nombre: String, descripcion: String, actual: Producto? = null, alGuardar: () -> Unit = {}) {
        val limpio = nombre.trim()
        if (limpio.isEmpty()) { mostrarError("Escribe el nombre de la película."); return }
        if (limpio.length > 20) { mostrarError("El nombre no puede superar 20 caracteres."); return }
        ejecutarOperacion(if (actual == null) "Película agregada." else "Película actualizada.", alGuardar) {
            repository.guardarProducto(Producto(actual?.id ?: 0, limpio, descripcion.trim()))
        }
    }
    fun eliminar(producto: Producto) = ejecutarOperacion("Película eliminada.") { repository.eliminarProducto(producto) }
    fun guardarGenero(nombre: String, id: Int = 0, alGuardar: () -> Unit = {}): Boolean {
        val limpio = nombre.trim()
        if (limpio.isEmpty()) { mostrarError("Escribe el nombre del género."); return false }
        if (limpio.length > 40) { mostrarError("El nombre del género no puede superar 40 caracteres."); return false }
        ejecutarOperacion(if (id == 0) "Género agregado." else "Género actualizado.", alGuardar) { catalogo.guardarGenero(Genero(id, limpio)) }
        return true
    }
    fun eliminarGenero(item: Genero) = ejecutarOperacion("Género eliminado.") { catalogo.borrarGenero(item) }
    fun guardarUsuario(nombre: String, correo: String, id: Int = 0, alGuardar: () -> Unit = {}): Boolean {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim()
        if (nombreLimpio.isEmpty()) { mostrarError("Escribe el nombre del usuario."); return false }
        if (nombreLimpio.length > 60) { mostrarError("El nombre del usuario no puede superar 60 caracteres."); return false }
        if (correoLimpio.length > 120 || !correoLimpio.matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))) {
            mostrarError("Escribe un correo válido de hasta 120 caracteres."); return false
        }
        ejecutarOperacion(if (id == 0) "Usuario agregado." else "Usuario actualizado.", alGuardar) { catalogo.guardarUsuario(Usuario(id, nombreLimpio, correoLimpio)) }
        return true
    }
    fun eliminarUsuario(item: Usuario) = ejecutarOperacion("Usuario eliminado.") { catalogo.borrarUsuario(item) }
    fun guardarResena(pelicula: String, usuario: String, puntuacion: Int, comentario: String, id: Int = 0, alGuardar: () -> Unit = {}): Boolean {
        val peliculaLimpia = pelicula.trim()
        val usuarioLimpio = usuario.trim()
        if (peliculaLimpia.isEmpty()) { mostrarError("Escribe el título de la película."); return false }
        if (usuarioLimpio.isEmpty()) { mostrarError("Escribe el nombre del usuario."); return false }
        if (puntuacion !in 1..5) { mostrarError("La puntuación debe ser un número del 1 al 5."); return false }
        if (peliculaLimpia.length > 100 || usuarioLimpio.length > 60 || comentario.length > 500) {
            mostrarError("Revisa los límites: título 100, usuario 60 y comentario 500 caracteres."); return false
        }
        ejecutarOperacion(if (id == 0) "Reseña agregada." else "Reseña actualizada.", alGuardar) { catalogo.guardarResena(Resena(id, peliculaLimpia, usuarioLimpio, puntuacion, comentario.trim())) }
        return true
    }
    fun eliminarResena(item: Resena) = ejecutarOperacion("Reseña eliminada.") { catalogo.borrarResena(item) }

    private fun ejecutarOperacion(mensajeExito: String, alGuardar: () -> Unit = {}, operacion: suspend () -> Unit) {
        viewModelScope.launch {
            _mensaje.value = null
            try {
                operacion()
                _mensaje.value = MensajeUi(mensajeExito, esError = false)
                alGuardar()
            } catch (_: Exception) {
                mostrarError("No se pudo guardar el cambio. Revisa los datos e inténtalo de nuevo.")
            }
        }
    }

    private fun mostrarError(texto: String) { _mensaje.value = MensajeUi(texto, esError = true) }
}
