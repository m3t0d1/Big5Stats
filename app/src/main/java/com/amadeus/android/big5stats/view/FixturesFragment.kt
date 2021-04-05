package com.amadeus.android.big5stats.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.model.League
import com.amadeus.android.big5stats.util.Resource
import com.amadeus.android.big5stats.viewModel.FixturesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_fixtures.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FixturesFragment : Fragment(R.layout.fragment_fixtures) {

    private val viewModel: FixturesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.currentLeague.collect {
                when (it) {
                    League.EMPTY -> { /*Do nothing*/ }
                    else -> setupSpinner(it)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.currentMatchDay.collect {
                if (it > 0) {
                    spinner_match_day.setSelection(it - 1)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.fixturesResource.collect { resource ->
                text_fixtures?.text = when(resource) {
                    is Resource.Loading -> {
                        swipe_to_refresh_fixtures?.isRefreshing = true
                        getString(R.string.loading)
                    }
                    is Resource.Error -> {
                        swipe_to_refresh_fixtures?.isRefreshing = false
                        getString(R.string.error)
                    }
                    is Resource.Success -> {
                        swipe_to_refresh_fixtures?.isRefreshing = false
                        resource.data
                    }
                    else -> {
                        swipe_to_refresh_fixtures?.isRefreshing = false
                        getString(R.string.title_fixtures)
                    }
                }
            }
        }
        swipe_to_refresh_fixtures?.setOnRefreshListener {
            val matchDay = spinner_match_day.selectedItemPosition + 1
            viewModel.fetchFixturesForMatchDay(matchDay, true)
        }
    }

    private fun setupSpinner(league: League) {
        val arrayResource = when (league) {
            League.BUNDESLIGA -> R.array.match_days_eighteen_teams_array
            else -> R.array.match_days_twenty_teams_array
        }
        ArrayAdapter.createFromResource(
                requireContext(),
                arrayResource,
                R.layout.spinner_item_match_day
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_match_day?.adapter = adapter
            spinner_match_day?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) { }

                override fun onItemSelected(p0: AdapterView<*>?, vuew: View?, position: Int, id: Long) {
                    if (viewModel.currentMatchDay.value > 0) {
                        viewModel.fetchFixturesForMatchDay(position + 1, false)
                    }
                }
            }
        }
    }

}