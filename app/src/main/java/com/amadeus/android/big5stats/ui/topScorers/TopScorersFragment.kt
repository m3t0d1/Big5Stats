package com.amadeus.android.big5stats.ui.topScorers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amadeus.android.big5stats.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_scorers.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TopScorersFragment : Fragment(R.layout.fragment_top_scorers) {

    private val topScorersViewModel: TopScorersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            topScorersViewModel.text.collect {
                text_top_scorers?.text = it
            }
        }
    }
}