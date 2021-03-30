package com.amadeus.android.big5stats.model

data class StandingsResponse(
    val competition: Competition,
    val filters: Filters,
    val season: Season,
    val standings: List<Standing>
)