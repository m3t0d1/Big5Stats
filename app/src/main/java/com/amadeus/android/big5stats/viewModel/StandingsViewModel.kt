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
import kotlinx.coroutines.flow.*
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
        viewModelScope.launch {
            repository.getOrFetchStandings(false)
        }
        viewModelScope.launch {
            repository.getSelectedLeagueFlow().collect {
                val response = repository.getOrFetchStandings(false)
                _standingsResource.emit(processStandingsResponse(response))
            }
        }
        viewModelScope.launch {
            _standingsResource.emitAll(
                repository.getStandings().map {
                    processStandingsResponse(it)
                }
            )
        }
    }

    private fun processStandingsResponse(response: StandingsResponse?): Resource<String> {
        if (response == null) {
            return Resource.Error("")
        }
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
        return Resource.Success(teamsList.toString())
    }
}