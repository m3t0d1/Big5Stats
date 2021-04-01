package com.amadeus.android.big5stats.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amadeus.android.big5stats.Big5StatsApplication
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.TopScorersResponse
import com.amadeus.android.big5stats.repository.FootballDataRepository
import com.amadeus.android.big5stats.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TopScorersViewModel
    @Inject constructor(
        application: Application,
        private val repository: FootballDataRepository
    ): AndroidViewModel(application) {

    private val _topScorersResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())
    val topScorersResource: StateFlow<Resource<String>> = _topScorersResource

    init {
        viewModelScope.launch {
            repository.getOrFetchTopScorers(false)
        }
        viewModelScope.launch {
            repository.getSelectedLeagueFlow().collect {
                val response = repository.getOrFetchTopScorers(false)
                _topScorersResource.emit(processTopScorersResponse(response))
            }
        }
        viewModelScope.launch {
            _topScorersResource.emitAll(
                repository.getTopScorers().map {
                    processTopScorersResponse(it)
                }
            )
        }
    }

    private fun processTopScorersResponse(response: TopScorersResponse?): Resource<String> {
        if (response == null) {
            return Resource.Error("")
        }
        val scorersList = StringBuilder()
        scorersList.appendLine("TOP SCORERS")
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
            scorersList.append(getApplication<Big5StatsApplication>().getString(R.string.title_top_scorers))
        }
        return Resource.Success(scorersList.toString())
    }

}