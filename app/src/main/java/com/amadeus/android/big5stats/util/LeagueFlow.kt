package com.amadeus.android.big5stats.util

import android.content.SharedPreferences
import com.amadeus.android.big5stats.model.League
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun SharedPreferences.leagueFlow() = callbackFlow {
    // A new Flow is created. This code executes in a coroutine!

    // 1. Create callback and add elements into the flow
    val callback =
        SharedPreferences.OnSharedPreferenceChangeListener { preference, key ->
            try {
                preference?.let {
                    if (key == Constants.SELECTED_LEAGUE_PREFERENCE_NAME) {
                        offer(League.values()[preference.getInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, 0)])
                    }
                }
            } catch (t: Throwable) {

            }
        }

    // 2. Register the callback to get shared preference updates
    registerOnSharedPreferenceChangeListener(callback)

    // 3. Wait for the consumer to cancel the coroutine and unregister
    // the callback. This suspends the coroutine until the Flow is closed.
    awaitClose {
        // Clean up code goes here
        unregisterOnSharedPreferenceChangeListener(callback)
    }
}