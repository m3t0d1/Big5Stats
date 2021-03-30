package com.amadeus.android.big5stats.model

data class CompetitionResponse(
    val area: Area,
    val code: Any,
    val currentSeason: CurrentSeason,
    val id: Int,
    val lastUpdated: String,
    val name: String,
    val plan: String,
    val seasons: List<Season>
)