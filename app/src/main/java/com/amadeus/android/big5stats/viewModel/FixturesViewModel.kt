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
class FixturesViewModel
    @Inject constructor(
        application: Application,
        private val repository: FootballDataRepository
    ): AndroidViewModel(application) {

    val fixturesResource = repository.fixturesResource.asLiveData()
    val currentLeague = repository.currentLeague.asLiveData()
    val currentMatchDay = repository.currentMatchDay.asLiveData()

    init {
        viewModelScope.launch {
            repository.collectFixtures()
        }
    }

    fun refreshFixtures() = viewModelScope.launch {
        repository.getOrFetchFixtures(true)
    }

    fun getOrFetchFixtures(matchDay: Int) = viewModelScope.launch {
        repository.getOrFetchFixtures(false, matchDay)
    }

}