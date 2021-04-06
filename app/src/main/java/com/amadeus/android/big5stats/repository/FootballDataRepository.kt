package com.amadeus.android.big5stats.repository

import android.content.Context
import android.content.SharedPreferences
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.api.FootballDataService
import com.amadeus.android.big5stats.db.CompetitionsDao
import com.amadeus.android.big5stats.db.FixturesDao
import com.amadeus.android.big5stats.db.StandingsDao
import com.amadeus.android.big5stats.db.TopScorersDao
import com.amadeus.android.big5stats.model.*
import com.amadeus.android.big5stats.util.Constants
import com.amadeus.android.big5stats.util.Resource
import com.amadeus.android.big5stats.util.leagueFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FootballDataRepository @Inject constructor(
    private val footballDataService: FootballDataService,
    private val competitionsDao: CompetitionsDao,
    private val fixturesDao: FixturesDao,
    private val standingsDao: StandingsDao,
    private val topScorersDao: TopScorersDao,
    private val sharedPreferences: SharedPreferences,
    private val application: Context
) {

    private val _fixturesResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())
    private val _currentMatchDay: MutableStateFlow<Int>
            = MutableStateFlow(0)
    private val _currentLeague: MutableStateFlow<League>
            = MutableStateFlow(League.EMPTY)
    private val _standingsResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())
    private val _topScorersResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())

    val fixturesResource: StateFlow<Resource<String>> = _fixturesResource
    val currentMatchDay: StateFlow<Int> = _currentMatchDay
    val currentLeague: StateFlow<League> = _currentLeague
    val standingResource: Flow<Resource<String>> = _standingsResource
    val topScorersResource: StateFlow<Resource<String>> = _topScorersResource

    @ExperimentalCoroutinesApi
    fun getSelectedLeagueFlow() = sharedPreferences.leagueFlow()

    fun getSelectedLeague() = League.values()[sharedPreferences.getInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, 0)]

    suspend fun collectFixtures() {
        getSelectedLeagueFlow()
                .onStart {
                    emit(getSelectedLeague())
                }.collect {
                    _currentLeague.emit(it)
                    getOrFetchFixtures(false)
                }
    }

    suspend fun collectStandings() {
        getSelectedLeagueFlow()
                .onStart {
                    emit(getSelectedLeague())
                }.collect {
                    getOrFetchStandings(false)
                }
    }

    suspend fun collectTopScorers() {
        getSelectedLeagueFlow()
                .onStart {
                    emit(getSelectedLeague())
                }.collect {
                    getOrFetchTopScorers(false)
                }
    }

    suspend fun getOrFetchFixtures(refresh: Boolean, matchDay: Int = 0) {
        val league = getSelectedLeague()
        val selectedMatchDay = if (matchDay == 0) {
            getOrFetchCompetition(false)
            _currentMatchDay.value
        } else {
            matchDay
        }
        if (refresh || fixturesDao.getFixturesForLeague(league.code, selectedMatchDay) == null) {
            val response = footballDataService.getMatchesForLeague(league.code, matchDay)
            if (response.isSuccessful && response.body() != null) {
                fixturesDao.insertFixtures(FixturesEntity(league.code, selectedMatchDay, true, response.body()!!))
            }
        }
        val dbData = fixturesDao.getFixturesForLeague(league.code, selectedMatchDay)?.response
        _fixturesResource.emit(processFixturesResponse(dbData))
    }

    suspend fun getOrFetchStandings(refresh: Boolean) {
        val league = getSelectedLeague()
        if (refresh || standingsDao.getStandingsForLeague(league.code) == null) {
            val response = footballDataService.getStandingsForLeague(league.code)
            if (response.isSuccessful && response.body() != null) {
                standingsDao.insertStandings(StandingsEntity(league.code, response.body()!!))
            }
        }
        val dbData = standingsDao.getStandingsForLeague(league.code)?.response
        _standingsResource.emit(processStandingsResponse(dbData))
    }

    suspend fun getOrFetchTopScorers(refresh: Boolean) {
        val league = getSelectedLeague()
        if (refresh || topScorersDao.getTopScorersForLeague(league.code) == null) {
            val response = footballDataService.getTopScorersForLeague(league.code)
            if (response.isSuccessful && response.body() != null) {
                topScorersDao.insertTopScorers(TopScorersEntity(league.code, response.body()!!))
            }
        }
        val dbData = topScorersDao.getTopScorersForLeague(league.code)?.response
        _topScorersResource.emit(processTopScorersResponse(dbData))
    }

    private suspend fun getOrFetchCompetition(refresh: Boolean) {
        val league = getSelectedLeague()
        if (refresh || competitionsDao.getCompetitionByCode(league.code) == null) {
            val response = footballDataService.getLeagueInfo(league.code)
            if (response.isSuccessful && response.body() != null) {
                competitionsDao.insertCompetitions(CompetitionsEntity(league.code, response.body()!!))
            }
        }
        val dbData = competitionsDao.getCompetitionByCode(league.code)?.response
        _currentMatchDay.emit(getCurrentMatchDay(dbData))
    }

    private fun getCurrentMatchDay(response: CompetitionResponse?): Int {
        return response?.currentSeason?.currentMatchday ?: 1
    }

    private fun processFixturesResponse(response: MatchesResponse?): Resource<String> {
        if (response == null) {
            return Resource.Error("")
        }
        val fixturesList = StringBuilder()
        for (match in response.matches) {
            val homeTeam = match.homeTeam.name
            val awayTeam = match.awayTeam.name
            val homeTeamScore = match.score?.fullTime?.homeTeam
            val awayTeamScore = match.score?.fullTime?.awayTeam
            val fixture = "$homeTeam $homeTeamScore:$awayTeamScore $awayTeam"
            fixturesList.appendLine(fixture)
            fixturesList.appendLine()
        }
        return Resource.Success(fixturesList.toString())
    }

    private fun processStandingsResponse(response: StandingsResponse?): Resource<String> {
        if (response == null) {
            return Resource.Error("")
        }
        val teamsList = StringBuilder()
        teamsList.appendLine(application.getString(R.string.title_standings))
        teamsList.appendLine()
        teamsList.appendLine()
        for (table in response.standings.first().table) {
            val position = table.position
            val name = table.team.name
            val points = table.points
            teamsList.appendLine("$position. $name $points points")
            teamsList.appendLine()
        }
        return Resource.Success(teamsList.toString())
    }

    private fun processTopScorersResponse(response: TopScorersResponse?): Resource<String> {
        if (response == null) {
            return Resource.Error("")
        }
        val scorersList = StringBuilder()
        scorersList.appendLine(application.getString(R.string.title_top_scorers))
        scorersList.appendLine()
        scorersList.appendLine()
        for (scorer in response.scorers) {
            val playerName = scorer.player.name
            val playerTeam = scorer.team.name
            val goalsScored = scorer.numberOfGoals
            scorersList.appendLine("$goalsScored $playerName ($playerTeam)")
            scorersList.appendLine()
        }
        if (scorersList.isBlank()) {
            scorersList.append(application.getString(R.string.title_top_scorers))
        }
        return Resource.Success(scorersList.toString())
    }

}