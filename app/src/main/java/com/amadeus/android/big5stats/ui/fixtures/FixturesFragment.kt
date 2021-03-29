package com.amadeus.android.big5stats.ui.fixtures

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amadeus.android.big5stats.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_fixtures.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FixturesFragment : Fragment(R.layout.fragment_fixtures) {

    private val fixturesViewModel: FixturesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            fixturesViewModel.text.collect {
                text_fixtures?.text = it
            }
        }
    }

}