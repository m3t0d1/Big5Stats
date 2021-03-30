package com.amadeus.android.big5stats.model

data class TopScorersResponse(
    val competition: Competition,
    val count: Int,
    val filters: Filters,
    val scorers: List<Scorer>,
    val season: Season
)