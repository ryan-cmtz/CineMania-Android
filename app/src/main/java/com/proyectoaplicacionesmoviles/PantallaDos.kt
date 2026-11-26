package com.proyectoaplicacionesmoviles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.proyectoaplicacionesmoviles.domain.model.*

private val secciones = listOf("Películas", "Géneros", "Usuarios", "Reseñas")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDos(navController: NavController, viewModel: ProductoViewModel = hiltViewModel()) {
    val productos by viewModel.productos.collectAsState()
    val generos by viewModel.generos.collectAsState()
    val usuarios by viewModel.usuarios.collectAsState()
    val resenas by viewModel.resenas.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    var seccion by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(topBar = { TopAppBar(title = { Text("CineMania | Catálogo") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            PrimaryScrollableTabRow(selectedTabIndex = seccion) {
                secciones.forEachIndexed { index, label ->
                    Tab(selected = seccion == index, onClick = { seccion = index }, text = { Text(label) })
                }
            }
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
                when (seccion) {
                    0 -> ProductosPanel(productos, viewModel)
                    1 -> GenerosPanel(generos, viewModel)
                    2 -> UsuariosPanel(usuarios, viewModel)
                    3 -> ResenasPanel(resenas, viewModel)
                }
                mensaje?.let { aviso ->
                    Spacer(Modifier.height(8.dp))
                    Text(aviso.texto, color = if (aviso.esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                    TextButton(onClick = viewModel::limpiarMensaje) { Text("Entendido") }
                }
            }
            OutlinedButton(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text("Volver al inicio")
            }
        }
    }
}

@Composable
private fun ProductosPanel(items: List<Producto>, vm: ProductoViewModel) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var editId by rememberSaveable { mutableIntStateOf(0) }
    Text("Películas", style = MaterialTheme.typography.headlineSmall)
    OutlinedTextField(nombre, { if (it.length <= 20) nombre = it }, label = { Text("Nombre (máx. 20)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
    Button(onClick = { vm.guardar(nombre, descripcion, items.firstOrNull { it.id == editId }) { nombre = ""; descripcion = ""; editId = 0 } }, enabled = nombre.isNotBlank()) { Text(if (editId == 0) "Agregar película" else "Guardar cambios") }
    items.forEach { item -> ItemCard("${item.nombre}\n${item.descripcion}", onEdit = { editId = item.id; nombre = item.nombre; descripcion = item.descripcion }, onDelete = { vm.eliminar(item) }) }
}

@Composable
private fun GenerosPanel(items: List<Genero>, vm: ProductoViewModel) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var editId by rememberSaveable { mutableIntStateOf(0) }
    Text("Géneros", style = MaterialTheme.typography.headlineSmall)
    OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre del género (máx. 40)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Button(onClick = { vm.guardarGenero(nombre, editId) { nombre = ""; editId = 0 } }, enabled = nombre.isNotBlank()) { Text(if (editId == 0) "Agregar género" else "Guardar cambios") }
    items.forEach { item -> ItemCard(item.nombre, onEdit = { editId = item.id; nombre = item.nombre }, onDelete = { vm.eliminarGenero(item) }) }
}

@Composable
private fun UsuariosPanel(items: List<Usuario>, vm: ProductoViewModel) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var editId by rememberSaveable { mutableIntStateOf(0) }
    Text("Usuarios", style = MaterialTheme.typography.headlineSmall)
    OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre (máx. 60)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    OutlinedTextField(correo, { correo = it }, label = { Text("Correo (máx. 120)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Button(onClick = { vm.guardarUsuario(nombre, correo, editId) { nombre = ""; correo = ""; editId = 0 } }, enabled = nombre.isNotBlank() && correo.isNotBlank()) { Text(if (editId == 0) "Agregar usuario" else "Guardar cambios") }
    items.forEach { item -> ItemCard("${item.nombre}\n${item.correo}", onEdit = { editId = item.id; nombre = item.nombre; correo = item.correo }, onDelete = { vm.eliminarUsuario(item) }) }
}

@Composable
private fun ResenasPanel(items: List<Resena>, vm: ProductoViewModel) {
    var pelicula by rememberSaveable { mutableStateOf("") }
    var usuario by rememberSaveable { mutableStateOf("") }
    var puntuacion by rememberSaveable { mutableStateOf("5") }
    var comentario by rememberSaveable { mutableStateOf("") }
    var editId by rememberSaveable { mutableIntStateOf(0) }
    Text("Reseñas", style = MaterialTheme.typography.headlineSmall)
    OutlinedTextField(pelicula, { pelicula = it }, label = { Text("Película (máx. 100)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    OutlinedTextField(usuario, { usuario = it }, label = { Text("Usuario (máx. 60)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    OutlinedTextField(puntuacion, { puntuacion = it.filter(Char::isDigit).take(1) }, label = { Text("Puntuación (1 a 5)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    OutlinedTextField(comentario, { comentario = it.take(500) }, label = { Text("Comentario (máx. 500)") }, modifier = Modifier.fillMaxWidth())
    Button(onClick = { vm.guardarResena(pelicula, usuario, puntuacion.toIntOrNull() ?: 0, comentario, editId) { pelicula = ""; usuario = ""; puntuacion = "5"; comentario = ""; editId = 0 } }, enabled = pelicula.isNotBlank() && usuario.isNotBlank()) { Text(if (editId == 0) "Agregar reseña" else "Guardar cambios") }
    items.forEach { item -> ItemCard("${item.pelicula} · ${item.puntuacion}/5\n${item.usuario}: ${item.comentario}", onEdit = { editId = item.id; pelicula = item.pelicula; usuario = item.usuario; puntuacion = item.puntuacion.toString(); comentario = item.comentario }, onDelete = { vm.eliminarResena(item) }) }
}

@Composable
private fun ItemCard(text: String, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(text)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEdit) { Text("Editar") }
                TextButton(onClick = onDelete) { Text("Eliminar") }
            }
        }
    }
}
