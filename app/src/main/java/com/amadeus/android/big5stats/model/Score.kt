package com.amadeus.android.big5stats.model

data class Score(
    val duration: String,
    val extraTime: ExtraTime,
    val fullTime: FullTime,
    val halfTime: HalfTime,
    val penalties: Penalties,
    val winner: String
)