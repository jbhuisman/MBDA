package com.mbda.kanyequotegenerator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mbda.kanyequotegenerator.data.model.QuoteItem
import com.mbda.kanyequotegenerator.ui.theme.DarkBlue800
import com.mbda.kanyequotegenerator.ui.theme.DarkBlue950
import com.mbda.kanyequotegenerator.ui.theme.Gray300
import com.mbda.kanyequotegenerator.ui.theme.OrangeAccent
import com.mbda.kanyequotegenerator.ui.theme.RedAccent
import com.mbda.kanyequotegenerator.ui.viewmodels.QuoteViewModel

@Composable
fun FavoritesScreen(viewModel: QuoteViewModel) {
    val favorites by viewModel.favorites.collectAsState(initial = emptyList())
    var editingQuote by remember { mutableStateOf<QuoteItem?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBlue800, DarkBlue950)
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(favorites) { quote ->
                ListItem(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Black.copy(alpha = 0.2f)
                    ),
                    headlineContent = {
                        Text(
                            text = quote.text,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = quote.author,
                            color = Gray300,
                            fontSize = 14.sp
                        )
                    },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { editingQuote = quote }) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = OrangeAccent)
                            }
                            IconButton(onClick = { viewModel.deleteQuote(quote) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = RedAccent)
                            }
                        }
                    }
                )
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