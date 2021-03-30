package com.amadeus.android.big5stats.repository

import android.content.SharedPreferences
import com.amadeus.android.big5stats.api.FootballDataService
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.TopScorersResponse
import com.amadeus.android.big5stats.util.Constants
import com.amadeus.android.big5stats.util.leagueFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Response
import javax.inject.Inject

class TopScorersRepository @Inject constructor(
    private val footballDataService: FootballDataService,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getTopScorersForLeague(league: League): Response<TopScorersResponse> {
        return footballDataService.getTopScorersForLeague(league.code)
    }

    @ExperimentalCoroutinesApi
    fun getSelectedLeagueFlow() = sharedPreferences.leagueFlow()

    fun getSelectedLeague() = League.values()[sharedPreferences.getInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, 0)]
}