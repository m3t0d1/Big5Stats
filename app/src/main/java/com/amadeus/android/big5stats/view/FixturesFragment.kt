package com.amadeus.android.big5stats.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amadeus.android.big5stats.R
import com.amadeus.android.big5stats.util.Resource
import com.amadeus.android.big5stats.viewModel.FixturesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_fixtures.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FixturesFragment : Fragment(R.layout.fragment_fixtures) {

    private val fixturesViewModel: FixturesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            fixturesViewModel.fixturesResource.collect { resource ->
                text_fixtures?.text = when(resource) {
                    is Resource.Loading -> getString(R.string.loading)
                    is Resource.Error -> getString(R.string.error)
                    is Resource.Success -> resource.data
                    else -> getString(R.string.title_fixtures)
                }
            }
        }
    }

}