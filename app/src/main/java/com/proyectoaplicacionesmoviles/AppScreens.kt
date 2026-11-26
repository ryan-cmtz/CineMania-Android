package com.proyectoaplicacionesmoviles
sealed class AppScreens(val route: String) {
    object PrimeraPantalla : AppScreens("Home")
    object SegundaPantalla : AppScreens("Detalle")
}