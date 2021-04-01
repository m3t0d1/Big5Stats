package com.amadeus.android.big5stats.db

import androidx.room.TypeConverter
import com.amadeus.android.big5stats.model.CompetitionResponse
import com.amadeus.android.big5stats.model.MatchesResponse
import com.amadeus.android.big5stats.model.StandingsResponse
import com.amadeus.android.big5stats.model.TopScorersResponse
import com.google.gson.Gson

class Converters {

    val gson = Gson()

    @TypeConverter
    fun fromCompetitions(competitionResponse: CompetitionResponse) = typeToString(competitionResponse)

    @TypeConverter
    fun toCompetitions(string: String) = stringToType<CompetitionResponse>(string)

    @TypeConverter
    fun fromFixtures(matchesResponse: MatchesResponse) = typeToString(matchesResponse)

    @TypeConverter
    fun toFixtures(string: String) = stringToType<MatchesResponse>(string)

    @TypeConverter
    fun fromStandings(standingsResponse: StandingsResponse) = typeToString(standingsResponse)

    @TypeConverter
    fun toStandings(string: String) = stringToType<StandingsResponse>(string)

    @TypeConverter
    fun fromTopScorers(topScorersResponse: TopScorersResponse) = typeToString(topScorersResponse)

    @TypeConverter
    fun toTopScorers(string: String) = stringToType<TopScorersResponse>(string)

    private fun <T: Any> typeToString(type: T): String {
        return gson.toJson(type)
    }

    private inline fun <reified T: Any> stringToType(string: String): T {
        return gson.fromJson(string, T::class.java)
    }
}