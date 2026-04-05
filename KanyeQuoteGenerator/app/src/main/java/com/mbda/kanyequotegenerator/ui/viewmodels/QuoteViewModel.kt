package com.mbda.kanyequotegenerator.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mbda.kanyequotegenerator.data.api.APIService
import com.mbda.kanyequotegenerator.data.local.AppDatabase
import com.mbda.kanyequotegenerator.data.model.QuoteItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuoteViewModel(application: Application) : AndroidViewModel(application) {
    private val quoteDao = AppDatabase.getDatabase(application).quoteDao()

    val favorites: Flow<List<QuoteItem>> = quoteDao.getAllQuotes()

    var currentQuoteText by mutableStateOf("Druk op de knop voor een quote")
    var currentQuoteAuthor by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun getKanyeQuote() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val quote = APIService.fetchKanyeQuote(getApplication())
                currentQuoteText = quote
                currentQuoteAuthor = "Kanye West"
            } catch (e: Exception) {
                errorMessage = "Kon Kanye niet bereiken: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun getRandomQuote() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val (quote, author) = APIService.fetchRandomQuote(getApplication())
                currentQuoteText = quote
                currentQuoteAuthor = author
            } catch (e: Exception) {
                errorMessage = "Fout bij ophalen random quote: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun saveCurrentQuote() {
        viewModelScope.launch {
            val newItem = QuoteItem(text = currentQuoteText, author = currentQuoteAuthor)
            quoteDao.insertQuote(newItem)
        }
    }

    fun addManualQuote(text: String, author: String) {
        viewModelScope.launch {
            quoteDao.insertQuote(QuoteItem(text = text, author = author))
        }
    }

    fun updateQuote(quote: QuoteItem) {
        viewModelScope.launch {
            quoteDao.updateQuote(quote)
        }
    }

    fun deleteQuote(quote: QuoteItem) {
        viewModelScope.launch {
            quoteDao.deleteQuote(quote)
        }
    }
}