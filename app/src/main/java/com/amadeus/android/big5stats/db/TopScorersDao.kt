package com.amadeus.android.big5stats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amadeus.android.big5stats.model.TopScorersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopScorersDao {
    @Query("SELECT * FROM top_scorers WHERE league_code = :leagueCode LIMIT 1")
    fun getTopScorersForLeagueFlow(leagueCode: String): Flow<TopScorersEntity?>

    @Query("SELECT * FROM top_scorers WHERE league_code = :leagueCode LIMIT 1")
    suspend fun getTopScorersForLeague(leagueCode: String): TopScorersEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopScorers(vararg topScorers: TopScorersEntity)
}