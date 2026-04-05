package com.mbda.kanyequotegenerator.data.model

// Kanye API geeft: { "quote": "..." }
data class KanyeResponse(val quote: String)

// ZenQuotes API geeft een lijst met: { "q": "...", "a": "..." }
data class ZenQuoteResponse(val q: String, val a: String)