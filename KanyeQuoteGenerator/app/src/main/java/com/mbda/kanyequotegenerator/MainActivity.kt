package com.mbda.kanyequotegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mbda.kanyequotegenerator.ui.screens.*
import com.mbda.kanyequotegenerator.ui.theme.KanyeQuoteGeneratorTheme
import com.mbda.kanyequotegenerator.ui.viewmodels.QuoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KanyeQuoteGeneratorTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: QuoteViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("generator") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("generator") {
            GeneratorScreen(
                viewModel = viewModel,
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("favorites") {
            FavoritesScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}