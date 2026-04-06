package com.mbda.kanyequotegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mbda.kanyequotegenerator.ui.screens.*
import com.mbda.kanyequotegenerator.ui.theme.*
import com.mbda.kanyequotegenerator.ui.viewmodels.QuoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Generator : BottomNavItem("generator", Icons.Default.Refresh, "Quote")
    object Favorites : BottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
    object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: QuoteViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        BottomNavItem.Generator.route,
        BottomNavItem.Favorites.route,
        BottomNavItem.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = DarkBlue950,
                    contentColor = Gray300,
                ) {
                    val items = listOf(
                        BottomNavItem.Generator,
                        BottomNavItem.Favorites,
                        BottomNavItem.Settings
                    )
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = OrangeAccent,
                                unselectedIconColor = Gray300,
                                selectedTextColor = OrangeAccent,
                                unselectedTextColor = Gray300,
                                indicatorColor = DarkBlue800
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(onTimeout = {
                    navController.navigate("generator") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("generator") {
                GeneratorScreen(viewModel = viewModel)
            }
            composable("favorites") {
                FavoritesScreen(viewModel = viewModel)
            }
            composable("settings") {
                SettingsScreen(onNavigateBack = { navController.navigate("generator") })
            }
        }
    }
}