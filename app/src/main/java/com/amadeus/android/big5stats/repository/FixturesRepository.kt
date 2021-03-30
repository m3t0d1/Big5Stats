package com.amadeus.android.big5stats.repository

import android.content.SharedPreferences
import com.amadeus.android.big5stats.api.FootballDataService
import com.amadeus.android.big5stats.model.CompetitionResponse
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.MatchesResponse
import com.amadeus.android.big5stats.util.Constants
import com.amadeus.android.big5stats.util.leagueFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Response
import javax.inject.Inject

class FixturesRepository @Inject constructor(
    private val footballDataService: FootballDataService,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getFixturesForLeague(league: League, matchDay: Int): Response<MatchesResponse> {
        return footballDataService.getMatchesForLeague(league.code, matchDay)
    }

    suspend fun getLeagueInfo(league: League): Response<CompetitionResponse> {
        return footballDataService.getLeagueInfo(league.code)
    }

    @ExperimentalCoroutinesApi
    fun getSelectedLeagueFlow() = sharedPreferences.leagueFlow()

    fun getSelectedLeague() = League.values()[sharedPreferences.getInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, 0)]
}