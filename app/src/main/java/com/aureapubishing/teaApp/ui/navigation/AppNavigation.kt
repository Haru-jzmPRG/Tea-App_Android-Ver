package com.aureapubishing.teaApp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aureapubishing.teaApp.ui.screens.home.HomeScreen
import com.aureapubishing.teaApp.ui.screens.infusiones.InfusionesScreen
import com.aureapubishing.teaApp.ui.screens.perfil.PerfilScreen
import com.aureapubishing.teaApp.ui.screens.tes.TesScreen

sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Inicio")
    data object Infusiones : Screen("infusiones", "Infusiones")
    data object Tes : Screen("tes", "Tés")
    data object Perfil : Screen("perfil", "Perfil")
}

val bottomBarScreens = listOf(Screen.Home, Screen.Infusiones, Screen.Tes, Screen.Perfil)

fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(onNext = { navController.navigateToTab(Screen.Infusiones.route) })
        }
        composable(Screen.Infusiones.route) {
            InfusionesScreen(onNext = { navController.navigateToTab(Screen.Tes.route) })
        }
        composable(Screen.Tes.route) {
            TesScreen(onNext = { navController.navigateToTab(Screen.Perfil.route) })
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(onNext = { navController.navigateToTab(Screen.Home.route) })
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomBarScreens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = { navController.navigateToTab(screen.route) },
                        icon = {},
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(navController, Modifier.padding(innerPadding))
    }
}