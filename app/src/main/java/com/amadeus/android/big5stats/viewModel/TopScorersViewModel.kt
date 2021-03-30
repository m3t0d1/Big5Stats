package com.amadeus.android.big5stats.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amadeus.android.big5stats.Big5StatsApplication
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.model.TopScorersResponse
import com.amadeus.android.big5stats.repository.TopScorersRepository
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
class TopScorersViewModel
    @Inject constructor(
        application: Application,
        private val topScorersRepository: TopScorersRepository
    ): AndroidViewModel(application) {

    private val _topScorersResource: MutableStateFlow<Resource<String>>
            = MutableStateFlow(Resource.Empty())
    val topScorersResource: StateFlow<Resource<String>> = _topScorersResource

    init {
        fetchTopScorersForLeague(topScorersRepository.getSelectedLeague())
        viewModelScope.launch{
            topScorersRepository.getSelectedLeagueFlow().collect{
                fetchTopScorersForLeague(it)
            }
        }
    }

    fun fetchTopScorersForLeague(league: League) = viewModelScope.launch {
        _topScorersResource.emit(Resource.Loading())
        val response = topScorersRepository.getTopScorersForLeague(league)
        if (response.isSuccessful && response.body() != null) {
            _topScorersResource.emit(Resource.Success(processTopScorersResponse(response.body()!!)))
        } else {
            _topScorersResource.emit(Resource.Error(response.message()))
        }
    }

    private fun processTopScorersResponse(response: TopScorersResponse): String {
        val scorersList = StringBuilder()
        for (scorer in response.scorers) {
            scorersList.appendLine(scorer.player.name)
        }
        if (scorersList.isBlank()) {
            scorersList.append(getApplication<Big5StatsApplication>().getString(R.string.title_top_scorers))
        }
        return scorersList.toString()
    }

}