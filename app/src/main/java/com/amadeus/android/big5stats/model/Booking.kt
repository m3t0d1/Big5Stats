package com.amadeus.android.big5stats.model

data class Booking(
    val card: String,
    val minute: Int,
    val player: Player,
    val team: Team
)