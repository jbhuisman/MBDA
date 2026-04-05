package com.mbda.kanyequotegenerator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mbda.kanyequotegenerator.ui.viewmodels.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorScreen(
    viewModel: QuoteViewModel,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kanye Generator") },
                actions = {
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "\"${viewModel.currentQuoteText}\"",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "- ${viewModel.currentQuoteAuthor}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            viewModel.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.getKanyeQuote() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Kanye Quote")
            }

            Button(
                onClick = { viewModel.getRandomQuote() },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Get Random Quote")
            }

            OutlinedButton(
                onClick = { viewModel.saveCurrentQuote() },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                enabled = viewModel.currentQuoteAuthor.isNotEmpty()
            ) {
                Text("Save to Favorites")
            }
        }
    }


}