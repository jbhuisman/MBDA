package com.mbda.kanyequotegenerator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mbda.kanyequotegenerator.data.model.QuoteItem
import com.mbda.kanyequotegenerator.ui.viewmodels.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: QuoteViewModel,
    onNavigateBack: () -> Unit
) {
    val favorites by viewModel.favorites.collectAsState(initial = emptyList())
    var editingQuote by remember { mutableStateOf<QuoteItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(favorites) { quote ->
                ListItem(
                    headlineContent = { Text(quote.text) },
                    supportingContent = { Text(quote.author) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { editingQuote = quote }) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }
                            IconButton(onClick = { viewModel.deleteQuote(quote) }) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }

    editingQuote?.let { quote ->
        AddEditQuoteDialog(
            quote = quote,
            onDismiss = { editingQuote = null },
            onConfirm = { text, author ->
                viewModel.updateQuote(quote.copy(text = text, author = author))
                editingQuote = null
            }
        )
    }
}