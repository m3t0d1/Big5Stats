package com.amadeus.android.big5stats

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        nav_view.setupWithNavController(nav_host_fragment.findNavController())

        setupSpinner()
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.leagues_array,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_leagues?.adapter = adapter
            spinner_leagues?.setSelection(mainViewModel.getSelectedLeague())
            spinner_leagues?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) { }

                override fun onItemSelected(p0: AdapterView<*>?, vuew: View?, position: Int, id: Long) {
                    mainViewModel.setSelectedLeague(position)
                }

            }
        }
    }
}