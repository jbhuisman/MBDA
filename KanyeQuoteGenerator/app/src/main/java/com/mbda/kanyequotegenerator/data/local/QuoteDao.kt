package com.mbda.kanyequotegenerator.data.local

import androidx.room.*
import com.mbda.kanyequotegenerator.data.model.QuoteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes ORDER BY savedDate DESC")
    fun getAllQuotes(): Flow<List<QuoteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteItem)

    @Delete
    suspend fun deleteQuote(quote: QuoteItem)

    @Update
    suspend fun updateQuote(quote: QuoteItem)
}