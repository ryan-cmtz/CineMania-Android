package com.proyectoaplicacionesmoviles

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.PrimeraPantalla.route) {
        composable(AppScreens.PrimeraPantalla.route) { PrimeraPantalla(navController) }
        composable(AppScreens.SegundaPantalla.route) { PantallaDos(navController) }
    }
}
