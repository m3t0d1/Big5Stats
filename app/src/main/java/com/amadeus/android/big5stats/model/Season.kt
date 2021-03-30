package com.amadeus.android.big5stats.model

data class Season(
    val availableStages: List<String>?,
    val currentMatchday: Int,
    val endDate: String,
    val id: Int,
    val startDate: String
)