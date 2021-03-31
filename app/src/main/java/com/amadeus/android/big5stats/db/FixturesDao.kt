package com.amadeus.android.big5stats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amadeus.android.big5stats.model.FixturesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FixturesDao {
    @Query("SELECT * FROM fixtures WHERE league_code = :leagueCode AND match_day = :matchDay LIMIT 1")
    fun getFixturesForLeague(leagueCode: String, matchDay: Int): Flow<FixturesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(vararg standings: FixturesEntity)
}