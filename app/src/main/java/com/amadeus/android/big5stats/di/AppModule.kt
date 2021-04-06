package com.amadeus.android.big5stats.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.amadeus.android.big5stats.api.FootballDataService
import com.amadeus.android.big5stats.db.FootballStatsDatabase
import com.amadeus.android.big5stats.util.Constants
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideFootballDatabase(@ApplicationContext context: Context)
            = Room.databaseBuilder(
        context,
        FootballStatsDatabase::class.java,
        Constants.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideCompetitionsDao(database: FootballStatsDatabase) = database.getCompetitionsDao()

    @Singleton
    @Provides
    fun provideFixturesDao(database: FootballStatsDatabase) = database.getFixturesDao()

    @Singleton
    @Provides
    fun provideStandingsDao(database: FootballStatsDatabase) = database.getStandingsDao()

    @Singleton
    @Provides
    fun provideTopScorersDao(database: FootballStatsDatabase) = database.getTopScorersDao()

    @Singleton
    @Provides
    fun provideLoggingInterceptor()
            = HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor)
            = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    @Singleton
    @Provides
    fun providerGsonConverterFactory() = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory)
            = Retrofit.Builder()
        .baseUrl(Constants.FOOTBALL_DATA_API_BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideFootballDataService(retrofit: Retrofit)
            = retrofit.create(FootballDataService::class.java)

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context)
            = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
}