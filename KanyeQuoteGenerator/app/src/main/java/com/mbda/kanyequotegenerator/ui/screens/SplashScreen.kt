package com.mbda.kanyequotegenerator.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSVuSlHzUyEdcQ_u2y7qiCZZD7EiH5gLsEAE2ABKhn2VnP77ROdPG_Bupn&s=10"

    LaunchedEffect(Unit) {
        val queue = Volley.newRequestQueue(context)
        val imageRequest = ImageRequest(
            imageUrl,
            { response -> bitmap = response },
            0, 0, null, null,
            { /* Foutafhandeling: laat zwart scherm zien */ }
        )
        queue.add(imageRequest)

        delay(3000)
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "KANYE QUOTE",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "GENERATOR",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

            CircularProgressIndicator(
                color = Color(0xFFFFA500),
                modifier = Modifier.size(40.dp)
            )
        }
    }
}