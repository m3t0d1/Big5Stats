package com.amadeus.android.big5stats.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amadeus.android.big5stats.model.CompetitionResponse
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.MatchesResponse
import com.amadeus.android.big5stats.repository.FootballDataRepository
import com.amadeus.android.big5stats.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FixturesViewModel
    @Inject constructor(
        application: Application,
        private val repository: FootballDataRepository
    ): AndroidViewModel(application) {

    private val _fixturesResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())
    private val _currentMatchDay: MutableStateFlow<Int>
            = MutableStateFlow(0)
    private val _currentLeague: MutableStateFlow<League>
            = MutableStateFlow(League.EMPTY)

    val fixturesResource: StateFlow<Resource<String>> = _fixturesResource
    val currentMatchDay: StateFlow<Int> = _currentMatchDay
    val currentLeague: StateFlow<League> = _currentLeague

    init {
        viewModelScope.launch {
            _currentLeague.emit(repository.getSelectedLeague())
        }
        viewModelScope.launch {
            val response = repository.getOrFetchCompetition(false)
            _currentMatchDay.emit(getCurrentMatchDay(response))
        }
        viewModelScope.launch {
            repository.getSelectedLeagueFlow().collect {
                val competitionResponse = repository.getOrFetchCompetition(false)
                val fixturesResponse = repository.getOrFetchFixtures(
                    getCurrentMatchDay(competitionResponse),
                    false
                )
                _fixturesResource.emit(processFixturesResponse(fixturesResponse))
            }
        }
        viewModelScope.launch {
            _fixturesResource.emitAll(
                repository.geFixtures(_currentMatchDay.value).map {
                    processFixturesResponse(it)
                }
            )
        }
    }

    fun fetchFixturesForMatchDay(matchDay: Int) = viewModelScope.launch {
        _fixturesResource.emit(Resource.Loading())
        _fixturesResource.emit(processFixturesResponse(repository.getOrFetchFixtures(matchDay, false)))
    }

    private fun fetchLeagueInfo(league: League) = viewModelScope.launch {
        _currentLeague.emit(league)
        _fixturesResource.emit(Resource.Loading())
        val leagueResponse = repository.getLeagueInfo(league)
        if (leagueResponse.isSuccessful && leagueResponse.body() != null) {
            _currentMatchDay.emit(getCurrentMatchDay(leagueResponse.body()!!))
        } else {
            _fixturesResource.emit(Resource.Error(leagueResponse.message()))
        }

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
}