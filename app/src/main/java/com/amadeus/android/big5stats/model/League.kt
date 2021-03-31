package com.amadeus.android.big5stats.model

import com.amadeus.android.big5stats.util.Constants

enum class League(
    val code: String
) {
    PREMIER_LEAGUE(Constants.PREMIER_LEAGUE_CODE),
    LA_LIGA(Constants.LA_LIGA_CODE),
    SERIE_A(Constants.SERIE_A_CODE),
    BUNDESLIGA(Constants.BUNDESLIGA_CODE),
    LIGUE_1(Constants.LIGUE_1_CODE),
    EMPTY("")
}