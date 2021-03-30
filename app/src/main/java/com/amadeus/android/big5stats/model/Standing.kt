package com.amadeus.android.big5stats.model

data class Standing(
    val group: Any,
    val stage: String,
    val table: List<Table>,
    val type: String
)