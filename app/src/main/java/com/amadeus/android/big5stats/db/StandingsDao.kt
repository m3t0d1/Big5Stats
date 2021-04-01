package com.amadeus.android.big5stats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amadeus.android.big5stats.model.StandingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StandingsDao {

    @Query("SELECT * FROM standings WHERE league_code = :leagueCode LIMIT 1")
    suspend fun getStandingsForLeague(leagueCode: String): StandingsEntity?

    @Query("SELECT * FROM standings WHERE league_code = :leagueCode LIMIT 1")
    fun getStandingsForLeagueFlow(leagueCode: String): Flow<StandingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandings(vararg standings: StandingsEntity)

}