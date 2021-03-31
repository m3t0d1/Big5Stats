package com.amadeus.android.big5stats.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.StandingsResponse
import com.amadeus.android.big5stats.repository.FootballDataRepository
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
class StandingsViewModel
    @Inject constructor(
        application: Application,
        private val repository: FootballDataRepository
    ): AndroidViewModel(application) {

    private val _standingsResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())
    val standingResource: StateFlow<Resource<String>> = _standingsResource

    init {
        fetchStandingsForLeague(repository.getSelectedLeague())
        viewModelScope.launch {
            repository.getSelectedLeagueFlow().collect {
                fetchStandingsForLeague(it)
            }
        }
    }

    fun fetchStandingsForLeague(league: League) = viewModelScope.launch {
        _standingsResource.emit(Resource.Loading())
        val response = repository.getStandingsForLeague(league)
        if (response.isSuccessful && response.body() != null) {
            _standingsResource.emit(Resource.Success(processStandingsResponse(response.body()!!)))
        } else {
            _standingsResource.emit(Resource.Error(response.message()))
        }
    }

    private fun processStandingsResponse(response: StandingsResponse): String {
        val teamsList = StringBuilder()
        teamsList.appendLine("STANDINGS")
        teamsList.appendLine()
        teamsList.appendLine()
        for (table in response.standings.first().table) {
            val position = table.position
            val name = table.team.name
            val points = table.points
            teamsList.appendLine("$position. $name $points points")
            teamsList.appendLine()
        }
        return teamsList.toString()
    }
}