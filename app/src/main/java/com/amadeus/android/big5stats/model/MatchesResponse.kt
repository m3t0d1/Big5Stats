package com.amadeus.android.big5stats.model

data class MatchesResponse(
    val competition: Competition,
    val count: Int,
    val filters: Filters,
    val matches: List<Match>
)