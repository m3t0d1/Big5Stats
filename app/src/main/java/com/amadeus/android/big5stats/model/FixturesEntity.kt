package com.amadeus.android.big5stats.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "fixtures",
    primaryKeys = ["league_code", "match_day"]
)
data class FixturesEntity(
    @ColumnInfo(name = "league_code") val leagueCode: String,
    @ColumnInfo(name = "match_day") val matchDay: Int,
    @ColumnInfo(name = "is_finished") val isFinished: Boolean,
    @ColumnInfo(name = "response") val response: MatchesResponse
)