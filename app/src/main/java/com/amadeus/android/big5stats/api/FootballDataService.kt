package com.amadeus.android.big5stats.api

import com.amadeus.android.big5stats.model.MatchesResponse
import com.amadeus.android.big5stats.model.StandingsResponse
import com.amadeus.android.big5stats.model.TopScorersResponse
import com.amadeus.android.big5stats.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface FootballDataService {

    @Headers("X-Auth-Token: ${Constants.FOOTBALL_DATA_API_TOKEN}")
    @GET("/v2/competitions/{leagueCode}/matches?matchday=1")
    suspend fun getMatchesForLeague(
        @Path("leagueCode")
        leagueCode: String
    ) : Response<MatchesResponse>

    @Headers("X-Auth-Token: ${Constants.FOOTBALL_DATA_API_TOKEN}")
    @GET("/v2/competitions/{leagueCode}/standings")
    suspend fun getStandingsForLeague(
        @Path("leagueCode")
        leagueCode: String
    ) : Response<StandingsResponse>

    @Headers("X-Auth-Token: ${Constants.FOOTBALL_DATA_API_TOKEN}")
    @GET("/v2/competitions/{leagueCode}/scorers")
    suspend fun getTopScorersForLeague(
        @Path("leagueCode")
        leagueCode: String
    ) : Response<TopScorersResponse>
}