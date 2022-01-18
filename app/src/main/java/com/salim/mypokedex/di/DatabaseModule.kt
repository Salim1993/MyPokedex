package com.salim.mypokedex.di

import android.content.Context
import androidx.room.Room
import com.salim.mypokedex.database.AppDatabase
import com.salim.mypokedex.database.AppDatabase.Companion.DATABASE_NAME
import com.salim.mypokedex.pokemon.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    fun providePokemonDao(database: AppDatabase): PokemonDao {
        return database.pokemonDao()
    }
}