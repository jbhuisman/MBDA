package com.mbda.kanyequotegenerator.ui.screens

import android.Manifest
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mbda.kanyequotegenerator.ui.theme.DarkBlue800
import com.mbda.kanyequotegenerator.ui.theme.DarkBlue950
import com.mbda.kanyequotegenerator.ui.theme.Gray300
import com.mbda.kanyequotegenerator.ui.theme.Gray600
import com.mbda.kanyequotegenerator.ui.theme.OrangeAccent
import com.mbda.kanyequotegenerator.ui.theme.RedAccent
import com.mbda.kanyequotegenerator.ui.viewmodels.QuoteViewModel

@Composable
fun GeneratorScreen(viewModel: QuoteViewModel) {
    val currentText = viewModel.currentQuoteText
    val currentAuthor = viewModel.currentQuoteAuthor
    val isLoading = viewModel.isLoading
    val favorites by viewModel.favorites.collectAsState(initial = emptyList())
    val isFavorite = favorites.any { it.text == currentText }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {}

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBlue800, DarkBlue950)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = OrangeAccent)
        } else {
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "SCORE: ${viewModel.score}",
                            color = OrangeAccent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "\"$currentText\"",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            lineHeight = 30.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (currentAuthor.isNotBlank() && currentAuthor != "???") {
                            Text(
                                text = "- $currentAuthor",
                                color = Gray300,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (viewModel.guessFeedback.isNotBlank()) {
                            Text(
                                text = viewModel.guessFeedback,
                                color = if (viewModel.guessFeedback.startsWith("Correct")) Color(0xFF4CAF50) else RedAccent,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (viewModel.showGuessButtons) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { viewModel.checkGuess(true) },
                                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                ) {
                                    Text("KANYE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                Button(
                                    onClick = { viewModel.checkGuess(false) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Gray600),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                ) {
                                    Text("OTHER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            }
                        } else {
                            if (currentText != "Druk op de knop voor een quote" && currentText.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        if (isFavorite) {
                                            val quoteToDelete = favorites.find { it.text == currentText }
                                            quoteToDelete?.let { viewModel.deleteQuote(it) }
                                        } else {
                                            viewModel.saveCurrentQuote()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(72.dp)
                                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (isFavorite) RedAccent else Color.White,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(
                                onClick = { viewModel.fetchGameQuote() },
                                colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = DarkBlue950,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "NEXT QUOTE",
                                    color = DarkBlue950,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 3.sp,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(32.dp)
                ) {
                    Text(
                        text = "SCORE: ${viewModel.score}",
                        color = OrangeAccent,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = "\"$currentText\"",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    if (currentAuthor.isNotBlank() && currentAuthor != "???") {
                        Text(
                            text = "- $currentAuthor",
                            color = Gray300,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    if (viewModel.guessFeedback.isNotBlank()) {
                        Text(
                            text = viewModel.guessFeedback,
                            color = if (viewModel.guessFeedback.startsWith("Correct")) Color(0xFF4CAF50) else RedAccent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    if (viewModel.showGuessButtons) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { viewModel.checkGuess(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue800),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                            ) {
                                Text("KANYE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Button(
                                onClick = { viewModel.checkGuess(false) },
                                colors = ButtonDefaults.buttonColors(containerColor = Gray600),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                            ) {
                                Text("OTHER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    } else {
                        if (currentText != "Druk op de knop voor een quote" && currentText.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    if (isFavorite) {
                                        val quoteToDelete = favorites.find { it.text == currentText }
                                        quoteToDelete?.let { viewModel.deleteQuote(it) }
                                    } else {
                                        viewModel.saveCurrentQuote()
                                    }
                                },
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isFavorite) RedAccent else Color.White,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(72.dp))
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        Button(
                            onClick = { viewModel.fetchGameQuote() },
                            colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = DarkBlue950,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "NEXT QUOTE",
                                color = DarkBlue950,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 3.sp,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}