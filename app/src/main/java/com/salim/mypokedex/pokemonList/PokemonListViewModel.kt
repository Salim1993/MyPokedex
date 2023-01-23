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

    private var lowerPokemonLimit = 1
    private var upperPokemonLimit = 151

    private val _pokemonList = MutableStateFlow(
        convertStringListToPokemonNameAndIdList(pokemonListUseCase.getPokemonListFlow().value)
    )
    val pokemonList = _pokemonList.asStateFlow()

    private val _showChangePokemonRangeDialog = MutableSharedFlow<Boolean>()
    val showChangePokemonRangeDialog = _showChangePokemonRangeDialog.asSharedFlow()

    private val _goToProfileEvent = MutableSharedFlow<Boolean>()
    val goToProfileEvent = _goToProfileEvent.asSharedFlow()

    // error states
    private val _isUpperLimitToHighFlow = MutableSharedFlow<Boolean>()
    val isUpperLimitToHighFlow = _isUpperLimitToHighFlow.asSharedFlow()

    private val _isLowerLimitToLowFlow = MutableSharedFlow<Boolean>()
    val isLowerLimitToLowFlow = _isLowerLimitToLowFlow.asSharedFlow()

    init {
        collectPokemonDataAndTransform()
    }

    private fun collectPokemonDataAndTransform() = viewModelScope.launch {
        pokemonListUseCase.getPokemonListFlow().collect { list ->
            _pokemonList.emit(convertStringListToPokemonNameAndIdList(list))
        }
    }

    private fun convertStringListToPokemonNameAndIdList(list: List<String>): List<PokemonNameAndId> {
        return list.mapIndexed { index, name ->
            // need to add offset to pokemon id, since that's where we start our list
            PokemonNameAndId(index + lowerPokemonLimit, name)
        }
    }

    fun getPokemonList() = viewModelScope.launch {
        // offset needs to also include pokemon on boundary of lower limit, so minus one to include that one
        pokemonListUseCase.getPokemonList(calculateNumberOfPokemonInList(), lowerPokemonLimit - 1)
    }

    fun setNewPokemonLimit(lowerLimit: Int, upperLimit: Int) = viewModelScope.launch {
        val upperCorrect = upperLimit <= ABSOLUTE_UPPER_LIMIT
        val lowerCorrect = lowerLimit >= ABSOLUTE_LOWER_LIMIT

        if(!upperCorrect) {
            _isUpperLimitToHighFlow.emit(true)
        }
        if(!lowerCorrect) {
            _isLowerLimitToLowFlow.emit(true)
        }
        if(upperCorrect && lowerCorrect) {
            lowerPokemonLimit = lowerLimit
            upperPokemonLimit = upperLimit
            getPokemonList()
        }
    }

    private fun calculateNumberOfPokemonInList(): Int {
        // add one to include the first pokemon that resides on boundary of lower limit
        return upperPokemonLimit - lowerPokemonLimit + 1
    }

    fun triggerShowChangePokemonRangeDialogEvent() = viewModelScope.launch {
        _showChangePokemonRangeDialog.emit(true)
    }

    fun getLowerPokemonLimit(): Int = lowerPokemonLimit
    fun getUpperPokemonLimit(): Int = upperPokemonLimit

    fun triggerGoToProfileEvent() = viewModelScope.launch {
        _goToProfileEvent.emit(true)
    }

    companion object {
        const val ABSOLUTE_LOWER_LIMIT = 1
        const val ABSOLUTE_UPPER_LIMIT = 1279
        const val BELOW_RANGE = 0
        const val ABOVE_RANGE = 1280
    }
}