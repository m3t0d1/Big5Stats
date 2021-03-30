package com.amadeus.android.big5stats.model

data class HomeTeam(
    val bench: List<Bench>,
    val captain: Captain,
    val coach: Coach,
    val id: Int,
    val lineup: List<Lineup>,
    val name: String
)