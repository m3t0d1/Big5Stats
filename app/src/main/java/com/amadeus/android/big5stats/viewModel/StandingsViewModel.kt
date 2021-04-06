package com.amadeus.android.big5stats.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.amadeus.android.big5stats.repository.FootballDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class StandingsViewModel
    @Inject constructor(
        application: Application,
        private val repository: FootballDataRepository
    ): AndroidViewModel(application) {

    val standingsResource = repository.standingResource.asLiveData()

    init {
        viewModelScope.launch {
            repository.collectStandings()
        }
    }

    fun refreshStandings() {
        viewModelScope.launch {
            repository.getOrFetchStandings(true)
        }
    }

}