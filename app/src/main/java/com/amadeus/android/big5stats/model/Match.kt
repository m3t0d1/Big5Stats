package com.amadeus.android.big5stats.model

data class Match(
    val attendance: Int,
    val awayTeam: AwayTeam,
    val bookings: List<Booking>,
    val goals: List<Goal>,
    val group: String,
    val homeTeam: HomeTeam,
    val id: Int,
    val lastUpdated: String,
    val matchday: Int,
    val referees: List<Referee>,
    val score: Score?,
    val season: Season,
    val stage: String,
    val status: String,
    val substitutions: List<Substitution>,
    val utcDate: String
)