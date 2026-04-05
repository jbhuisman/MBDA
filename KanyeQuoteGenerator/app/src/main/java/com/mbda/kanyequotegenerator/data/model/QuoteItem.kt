package com.mbda.kanyequotegenerator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "quotes")
data class QuoteItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val author: String,
    val savedDate: Long = System.currentTimeMillis()
)