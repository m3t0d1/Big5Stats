package com.amadeus.android.big5stats.ui.fixtures

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.amadeus.android.big5stats.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FixturesViewModel
    @Inject constructor(application: Application)
    : AndroidViewModel(application) {

    private val _text = MutableStateFlow(application.getString(R.string.title_fixtures))
    val text: StateFlow<String> = _text
}