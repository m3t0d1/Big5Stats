package com.amadeus.android.big5stats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amadeus.android.big5stats.model.CompetitionsEntity
import com.amadeus.android.big5stats.model.TopScorersEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface CompetitionsDao {
    @Query("SELECT * FROM competitions WHERE league_code = :leagueCode LIMIT 1")
    suspend fun getCompetitionByCode(leagueCode: String): CompetitionsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompetitions(vararg competitions: CompetitionsEntity)
}