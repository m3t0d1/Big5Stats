package com.amadeus.android.big5stats

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amadeus.android.big5stats.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
            R.layout.spinner_item_league
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_leagues?.adapter = adapter
            spinner_leagues?.setSelection(getSelectedLeague())
            spinner_leagues?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) { }

                override fun onItemSelected(p0: AdapterView<*>?, vuew: View?, position: Int, id: Long) {
                    setSelectedLeague(position)
                }
            }
        }
    }

    private fun setSelectedLeague(position: Int) {
        sharedPreferences.edit{
            putInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, position)
        }
    }

    private fun getSelectedLeague() : Int {
        return sharedPreferences.getInt(Constants.SELECTED_LEAGUE_PREFERENCE_NAME, 0)
    }
}