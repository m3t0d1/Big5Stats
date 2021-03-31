package com.amadeus.android.big5stats.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_scorers")
data class TopScorersEntity(
    @PrimaryKey
    @ColumnInfo(name = "league_code") val leagueCode: String,
    @ColumnInfo(name = "response") val response: TopScorersResponse
)