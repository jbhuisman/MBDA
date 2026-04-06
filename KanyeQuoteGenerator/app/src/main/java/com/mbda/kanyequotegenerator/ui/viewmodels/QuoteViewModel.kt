package com.mbda.kanyequotegenerator.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mbda.kanyequotegenerator.data.api.APIService
import com.mbda.kanyequotegenerator.data.local.AppDatabase
import com.mbda.kanyequotegenerator.data.local.PreferencesManager
import com.mbda.kanyequotegenerator.data.model.QuoteItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuoteViewModel(application: Application) : AndroidViewModel(application) {
    private val quoteDao = AppDatabase.getDatabase(application).quoteDao()
    private val prefs = PreferencesManager(application)

    val favorites: Flow<List<QuoteItem>> = quoteDao.getAllQuotes()

    var currentQuoteText by mutableStateOf("Druk op de knop voor een quote")
    var currentQuoteAuthor by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var score by mutableStateOf(prefs.getScore())
    var showGuessButtons by mutableStateOf(false)
    var guessFeedback by mutableStateOf("")
    var actualAuthor by mutableStateOf("")

    fun fetchGameQuote() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            showGuessButtons = false
            guessFeedback = ""
            currentQuoteAuthor = "???"

            try {
                val isKanye = (0..1).random() == 0
                if (isKanye) {
                    currentQuoteText = APIService.fetchKanyeQuote(getApplication())
                    actualAuthor = "Kanye West"
                } else {
                    val (quote, author) = APIService.fetchRandomQuote(getApplication())
                    currentQuoteText = quote
                    actualAuthor = author
                }
                showGuessButtons = true
            } catch (e: Exception) {
                errorMessage = "Fout bij ophalen quote: ${e.message}"
                currentQuoteText = "Er ging iets mis."
            } finally {
                isLoading = false
            }
        }
    }

    fun checkGuess(guessedKanye: Boolean) {
        val isActuallyKanye = actualAuthor == "Kanye West"
        if (guessedKanye == isActuallyKanye) {
            score += 1
            guessFeedback = "Correct! +1 Punt"
        } else {
            score = 0
            guessFeedback = "Fout! Het was $actualAuthor."
        }

        prefs.saveScore(score)

        currentQuoteAuthor = actualAuthor
        showGuessButtons = false
    }

    fun saveCurrentQuote() {
        viewModelScope.launch {
            val newItem = QuoteItem(text = currentQuoteText, author = currentQuoteAuthor)
            quoteDao.insertQuote(newItem)
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