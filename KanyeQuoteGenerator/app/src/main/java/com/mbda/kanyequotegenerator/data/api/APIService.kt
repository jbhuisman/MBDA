package com.mbda.kanyequotegenerator.data.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object APIService {

    suspend fun fetchKanyeQuote(context: Context): String = suspendCancellableCoroutine { continuation ->
        val url = "https://api.kanye.rest/"
        val queue = Volley.newRequestQueue(context)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    // Haal de "quote" string uit het JSON object
                    val quote = response.getString("quote")
                    continuation.resume(quote)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            },
            { error ->
                continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
            }
        )
        queue.add(request)
    }

    suspend fun fetchRandomQuote(context: Context): Pair<String, String> = suspendCancellableCoroutine { continuation ->
        val url = "https://zenquotes.io/api/random"
        val queue = Volley.newRequestQueue(context)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val firstItem = response.getJSONObject(0)
                    val quote = firstItem.getString("q")
                    val author = firstItem.getString("a")
                    continuation.resume(Pair(quote, author))
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            },
            { error ->
                continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
            }
        )
        queue.add(request)
    }
}