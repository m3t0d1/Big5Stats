package com.amadeus.android.big5stats.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.util.Resource
import com.amadeus.android.big5stats.viewModel.TopScorersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_scorers.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TopScorersFragment : Fragment(R.layout.fragment_top_scorers) {

    private val topScorersViewModel: TopScorersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            topScorersViewModel.topScorersResource.collect {resource ->
                text_top_scorers?.text = when(resource) {
                    is Resource.Loading -> getString(R.string.loading)
                    is Resource.Error -> resource.message
                    is Resource.Success -> resource.data
                    else -> getString(R.string.title_top_scorers)
                }
            }
        }
    }
}