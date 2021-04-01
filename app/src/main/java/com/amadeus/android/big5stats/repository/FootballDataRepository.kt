package com.amadeus.android.big5stats.repository

import android.content.SharedPreferences
import com.amadeus.android.big5stats.api.FootballDataService
import com.amadeus.android.big5stats.db.CompetitionsDao
import com.amadeus.android.big5stats.db.FixturesDao
import com.amadeus.android.big5stats.db.StandingsDao
import com.amadeus.android.big5stats.db.TopScorersDao
import com.amadeus.android.big5stats.model.*
import com.amadeus.android.big5stats.util.Constants
import com.amadeus.android.big5stats.util.leagueFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class FootballDataRepository @Inject constructor(
    private val footballDataService: FootballDataService,
    private val competitionsDao: CompetitionsDao,
    private val fixturesDao: FixturesDao,
    private val standingsDao: StandingsDao,
    private val topScorersDao: TopScorersDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getOrFetchCompetition(refresh: Boolean): CompetitionResponse? {
        val league = getSelectedLeague()
        if (refresh || competitionsDao.getCompetitionByCode(league.code) == null) {
            val response = footballDataService.getLeagueInfo(league.code)
            if (response.isSuccessful && response.body() != null) {
                competitionsDao.insertCompetitions(CompetitionsEntity(league.code, response.body()!!))
            }
        }
        return competitionsDao.getCompetitionByCode(league.code)?.response
    }

    suspend fun getOrFetchFixtures(matchDay: Int, refresh: Boolean): MatchesResponse? {
        val league = getSelectedLeague()
        if (refresh || fixturesDao.getFixturesForLeague(league.code, matchDay) == null) {
            val response = footballDataService.getMatchesForLeague(league.code, matchDay)
            if (response.isSuccessful && response.body() != null) {
                fixturesDao.insertFixtures(FixturesEntity(league.code, matchDay, true, response.body()!!))
            }
        }
        return fixturesDao.getFixturesForLeague(league.code, matchDay)?.response
    }

    suspend fun getOrFetchStandings(refresh: Boolean): StandingsResponse? {
        val league = getSelectedLeague()
        if (refresh ||  standingsDao.getStandingsForLeague(league.code) == null) {
            val response = footballDataService.getStandingsForLeague(league.code)
            if (response.isSuccessful && response.body() != null) {
                standingsDao.insertStandings(StandingsEntity(league.code, response.body()!!))
            }
        }
        return standingsDao.getStandingsForLeague(league.code)?.response
    }

    suspend fun getOrFetchTopScorers(refresh: Boolean): TopScorersResponse? {
        val league = getSelectedLeague()
        if (refresh || topScorersDao.getTopScorersForLeague(league.code) == null) {
            val response = footballDataService.getTopScorersForLeague(league.code)
            if (response.isSuccessful && response.body() != null) {
                topScorersDao.insertTopScorers(TopScorersEntity(league.code, response.body()!!))
            }
        }
        return topScorersDao.getTopScorersForLeague(league.code)?.response
    }

    fun geFixtures(matchDay: Int): Flow<MatchesResponse?> {
        return fixturesDao.getFixturesForLeagueFlow(getSelectedLeague().code, matchDay)
            .map {
                it?.response
            }
    }

    fun getTopScorers(): Flow<TopScorersResponse?> {
        return topScorersDao.getTopScorersForLeagueFlow(getSelectedLeague().code)
            .map {
                it?.response
            }
    }

    fun getStandings(): Flow<StandingsResponse?> {
        return standingsDao.getStandingsForLeagueFlow(getSelectedLeague().code)
            .map {
                it?.response
            }
    }

    suspend fun getLeagueInfo(league: League): Response<CompetitionResponse> {
        return footballDataService.getLeagueInfo(league.code)
    }

    @ExperimentalCoroutinesApi
    fun getSelectedLeagueFlow() = sharedPreferences.leagueFlow()

    fun getSelectedLeague() = League.values()[sharedPreferences.getInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, 0)]
}