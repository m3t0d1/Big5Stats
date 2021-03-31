package com.amadeus.android.big5stats.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amadeus.android.big5stats.model.FixturesEntity
import com.amadeus.android.big5stats.model.StandingsEntity
import com.amadeus.android.big5stats.model.TopScorersEntity

@Database(
    entities = [FixturesEntity::class, StandingsEntity::class, TopScorersEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FootballStatsDatabase: RoomDatabase() {

    abstract fun getFixturesDao(): FixturesDao

    abstract fun getStandingsDao(): StandingsDao

    abstract fun getTopScorersDao(): TopScorersDao
}