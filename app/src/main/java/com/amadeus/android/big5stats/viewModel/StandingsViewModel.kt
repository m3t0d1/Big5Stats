package com.amadeus.android.big5stats.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amadeus.android.big5stats.Big5StatsApplication
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.StandingsResponse
import com.amadeus.android.big5stats.repository.StandingsRepository
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
        private val standingsRepository: StandingsRepository
    ): AndroidViewModel(application) {

    private val _standingsResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())
    val standingResource: StateFlow<Resource<String>> = _standingsResource

    init {
        fetchStandingsForLeague(standingsRepository.getSelectedLeague())
        viewModelScope.launch {
            standingsRepository.getSelectedLeagueFlow().collect {
                fetchStandingsForLeague(it)
            }
        }
    }

    fun fetchStandingsForLeague(league: League) = viewModelScope.launch {
        _standingsResource.emit(Resource.Loading())
        val response = standingsRepository.getStandingsForLeague(league)
        if (response.isSuccessful && response.body() != null) {
            _standingsResource.emit(Resource.Success(processStandingsResponse(response.body()!!)))
        } else {
            _standingsResource.emit(Resource.Error(response.message()))
        }
    }

    private fun processStandingsResponse(response: StandingsResponse): String {
        val teamsList = StringBuilder()
        for (table in response.standings.first().table) {
            teamsList.appendLine(table.team.name)
        }
        if (teamsList.isBlank()) {
            teamsList.append(getApplication<Big5StatsApplication>().getString(R.string.title_standings))
        }
        return teamsList.toString()
    }
}