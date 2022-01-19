package com.salim.mypokedex.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.salim.mypokedex.database.AppDatabase
import com.salim.mypokedex.database.AppDatabase.Companion.DATABASE_NAME
import com.salim.mypokedex.pokemon.PokemonDao
import com.salim.mypokedex.utilities.SharedPreferencesWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferencesModule {

    private const val POKEMON_PREF = "POKEMON_PREF"

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext appContext: Context): SharedPreferencesWrapper {
        val prefs = appContext.getSharedPreferences(POKEMON_PREF, Context.MODE_PRIVATE)
        return SharedPreferencesWrapper(prefs)
    }
}