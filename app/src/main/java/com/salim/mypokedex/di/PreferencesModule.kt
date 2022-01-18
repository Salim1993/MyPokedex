package com.salim.mypokedex.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.salim.mypokedex.database.AppDatabase
import com.salim.mypokedex.database.AppDatabase.Companion.DATABASE_NAME
import com.salim.mypokedex.pokemon.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferencesModule {

    @ActivityScoped
    @Provides
    fun providePreferences(@ActivityContext activity: Activity): SharedPreferences {
        return activity.getPreferences(Context.MODE_PRIVATE)
    }
}