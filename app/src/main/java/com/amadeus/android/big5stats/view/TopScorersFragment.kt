package com.amadeus.android.big5stats.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.util.Resource
import com.amadeus.android.big5stats.viewModel.TopScorersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_scorers.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TopScorersFragment : Fragment(R.layout.fragment_top_scorers) {

    private val viewModel: TopScorersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.topScorersResource.observe(viewLifecycleOwner, Observer {
            resource ->
            text_top_scorers?.text = when(resource) {
                is Resource.Loading -> {
                    swipe_to_refresh_top_scorers?.isRefreshing = true
                    getString(R.string.loading)
                }
                is Resource.Error -> {
                    swipe_to_refresh_top_scorers?.isRefreshing = false
                    getString(R.string.error)
                }
                is Resource.Success -> {
                    swipe_to_refresh_top_scorers?.isRefreshing = false
                    resource.data
                }
                else -> {
                    swipe_to_refresh_top_scorers?.isRefreshing = false
                    getString(R.string.title_top_scorers)
                }
            }
        })
        swipe_to_refresh_top_scorers?.setOnRefreshListener {
            viewModel.refreshTopScorers()
        }
    }
}