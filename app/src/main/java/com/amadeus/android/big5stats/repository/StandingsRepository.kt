package com.amadeus.android.big5stats.repository

import android.content.SharedPreferences
import com.amadeus.android.big5stats.api.FootballDataService
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.StandingsResponse
import com.amadeus.android.big5stats.util.Constants
import com.amadeus.android.big5stats.util.leagueFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Response
import javax.inject.Inject

class StandingsRepository @Inject constructor(
    private val footballDataService: FootballDataService,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getStandingsForLeague(league: League): Response<StandingsResponse> {
        return footballDataService.getStandingsForLeague(league.code)
    }

    @ExperimentalCoroutinesApi
    fun getSelectedLeagueFlow() = sharedPreferences.leagueFlow()

    fun getSelectedLeague() = League.values()[sharedPreferences.getInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, 0)]
}