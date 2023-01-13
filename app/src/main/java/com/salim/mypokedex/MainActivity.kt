package com.salim.mypokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.salim.mypokedex.pokemonList.PokemonListFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}