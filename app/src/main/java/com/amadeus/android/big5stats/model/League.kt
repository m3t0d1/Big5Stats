package com.amadeus.android.big5stats.model

import com.amadeus.android.big5stats.util.Constants

enum class League(
    val code: String,
    val matchDays: Int
) {
    PREMIER_LEAGUE(Constants.PREMIER_LEAGUE_CODE, 38),
    LA_LIGA(Constants.LA_LIGA_CODE, 38),
    SERIE_A(Constants.SERIE_A_CODE, 38),
    BUNDESLIGA(Constants.BUNDESLIGA_CODE, 34),
    LIGUE_1(Constants.LIGUE_1_CODE, 38)
}