package com.amadeus.android.big5stats.ui.standings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amadeus.android.big5stats.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_standings.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StandingsFragment : Fragment(R.layout.fragment_standings) {

    private val standingsViewModel: StandingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            standingsViewModel.text.collect {
                text_standings?.text = it
            }
        }
    }
}