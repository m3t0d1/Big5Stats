package com.amadeus.android.big5stats.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amadeus.android.big5stats.Big5StatsApplication
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.model.CompetitionResponse
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.MatchesResponse
import com.amadeus.android.big5stats.repository.FixturesRepository
import com.amadeus.android.big5stats.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FixturesViewModel
    @Inject constructor(
        application: Application,
        private val fixturesRepository: FixturesRepository
    ): AndroidViewModel(application) {

    private val _fixturesResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())
    val fixturesResource: StateFlow<Resource<String>> = _fixturesResource

    init {
        fetchFixturesForLeague(fixturesRepository.getSelectedLeague())
        viewModelScope.launch {
            fixturesRepository.getSelectedLeagueFlow().collect {
                fetchFixturesForLeague(it)
            }
        }
    }

    fun fetchFixturesForLeague(league: League) = viewModelScope.launch {
        _fixturesResource.emit(Resource.Loading())
        val leagueResponse = fixturesRepository.getLeagueInfo(league)
        if (leagueResponse.isSuccessful && leagueResponse.body() != null) {
            val currentMatchDay = getCurrentMatchDay(leagueResponse.body()!!)
            val response
                    = fixturesRepository.getFixturesForLeague(league, currentMatchDay)
            if (response.isSuccessful && response.body() != null) {
                _fixturesResource.emit(Resource.Success(processFixturesResponse(response.body()!!)))
            } else {
                _fixturesResource.emit(Resource.Error(response.message()))
            }
        } else {
            _fixturesResource.emit(Resource.Error(leagueResponse.message()))
        }

    }

    private fun getCurrentMatchDay(response: CompetitionResponse): Int {
        return response.currentSeason.currentMatchday
    }

    private fun processFixturesResponse(response: MatchesResponse): String {
        val fixturesList = StringBuilder()
        fixturesList.appendLine("MATCH DAY ${response.matches.first().matchday}")
        fixturesList.appendLine()
        for (match in response.matches) {
            val homeTeam = match.homeTeam.name
            val awayTeam = match.awayTeam.name
            val homeTeamScore = match.score?.fullTime?.homeTeam
            val awayTeamScore = match.score?.fullTime?.awayTeam
            val fixture = "$homeTeam $homeTeamScore:$awayTeamScore $awayTeam"
            fixturesList.appendLine(fixture)
            fixturesList.appendLine()
        }
        return fixturesList.toString()
    }
}