package com.amadeus.android.big5stats.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.util.Resource
import com.amadeus.android.big5stats.viewModel.StandingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_standings.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class StandingsFragment : Fragment(R.layout.fragment_standings) {

    private val viewModel: StandingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.standingsResource.observe(
                viewLifecycleOwner,
                Observer { resource ->
                    text_standings.text = when(resource) {
                        is Resource.Loading -> {
                            swipe_to_refresh_standings?.isRefreshing = true
                            getString(R.string.loading)
                        }
                        is Resource.Error -> {
                            swipe_to_refresh_standings?.isRefreshing = false
                            getString(R.string.error)
                        }
                        is Resource.Success -> {
                            swipe_to_refresh_standings?.isRefreshing = false
                            resource.data
                        }
                        else -> {
                            swipe_to_refresh_standings?.isRefreshing = false
                            getString(R.string.title_standings)
                        }
                    }
                })
        swipe_to_refresh_standings?.setOnRefreshListener {
            viewModel.refreshStandings()
        }
    }
}