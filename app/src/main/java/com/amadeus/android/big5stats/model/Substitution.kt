package com.amadeus.android.big5stats.model

data class Substitution(
    val minute: Int,
    val playerIn: PlayerIn,
    val playerOut: PlayerOut,
    val team: Team
)