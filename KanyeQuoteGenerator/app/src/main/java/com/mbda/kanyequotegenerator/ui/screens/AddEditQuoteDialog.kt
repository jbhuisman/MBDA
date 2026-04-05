package com.mbda.kanyequotegenerator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mbda.kanyequotegenerator.data.model.QuoteItem

@Composable
fun AddEditQuoteDialog(
    quote: QuoteItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var text by remember { mutableStateOf(quote?.text ?: "") }
    var author by remember { mutableStateOf(quote?.author ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (quote == null) "Add Quote" else "Edit Quote") },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Quote") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text, author) },
                enabled = text.isNotBlank() && author.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}