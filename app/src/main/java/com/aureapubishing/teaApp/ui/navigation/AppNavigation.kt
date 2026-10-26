package com.aureapubishing.teaApp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aureapubishing.teaApp.ui.screens.home.HomeScreen
import com.aureapubishing.teaApp.ui.screens.infusions.InfusionsScreen
import com.aureapubishing.teaApp.ui.screens.profile.ProfileScreen
import com.aureapubishing.teaApp.ui.screens.teas.TeasScreen

sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Inicio")
    data object Infusions : Screen("infusiones", "Infusiones")
    data object Teas : Screen("tes", "Tés")
    data object Profile: Screen("perfil", "Perfil")
}

val bottomBarScreens = listOf(Screen.Home, Screen.Infusions, Screen.Teas, Screen.Profile)

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
            HomeScreen(onNext = { navController.navigateToTab(Screen.Infusions.route) })
        }
        composable(Screen.Infusions.route) {
            InfusionsScreen(onNext = { navController.navigateToTab(Screen.Teas.route) })
        }
        composable(Screen.Teas.route) {
            TeasScreen(onNext = { navController.navigateToTab(Screen.Profile.route) })
        }
        composable(Screen.Profile.route) {
            ProfileScreen(onNext = { navController.navigateToTab(Screen.Home.route) })
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