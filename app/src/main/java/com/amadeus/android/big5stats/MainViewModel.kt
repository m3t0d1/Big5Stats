package com.amadeus.android.big5stats

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import com.amadeus.android.big5stats.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(application: Application)
    : AndroidViewModel(application) {

    private val sharedPreferences : SharedPreferences? = application.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setSelectedLeague(position: Int) {
        sharedPreferences?.edit{
            putInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, position)
        }
    }

    fun getSelectedLeague() : Int {
        return sharedPreferences?.getInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, 0) ?: 0
    }
}