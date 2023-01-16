package com.salim.mypokedex.pokemonList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salim.mypokedex.pokemon.PokemonNameAndId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonNameAndId>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()

    init {
        collectPokemonDataAndTransform()
        getPokemonList()
    }

    private fun collectPokemonDataAndTransform() = viewModelScope.launch {
        pokemonListUseCase.pokemonListFlow.collect { list ->
            val newList = list.mapIndexed { index, name ->
                PokemonNameAndId(index + 1, name)
            }
            _pokemonList.emit(newList)
        }
    }

    private fun getPokemonList() = viewModelScope.launch {
        pokemonListUseCase.getOriginal151List()
    }
}