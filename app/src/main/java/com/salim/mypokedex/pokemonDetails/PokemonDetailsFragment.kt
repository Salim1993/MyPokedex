package com.salim.mypokedex.pokemonDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.salim.mypokedex.R
import com.salim.mypokedex.databinding.FragmentPokemonDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PokemonDetailsFragment : Fragment(R.layout.fragment_pokemon_details) {

    @Inject lateinit var getPokemonDetailsUseCaseFactory: GetPokemonDetailsUseCaseFactory

    private val viewModel: PokemonDetailsViewModel by viewModels {
        PokemonDetailViewModelFactory("", getPokemonDetailsUseCaseFactory)
    }
    private lateinit var binding: FragmentPokemonDetailsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPokemonDetailsBinding.bind(view)

        viewModel.pokemonDetailsFlow.asLiveData().observe(viewLifecycleOwner) { pokemon ->
            binding.textName.text = getString(R.string.name, pokemon.name)
            binding.textBaseExperience.text = getString(R.string.base_experience, pokemon.baseExperience)
            binding.textHeight.text = getString(R.string.height, pokemon.height)
            binding.textId.text = getString(R.string.id, pokemon.id)
            binding.textIsDefault.text = getString(R.string.is_default, pokemon.isDefault)
            binding.textLocationAreaEncounters.text = getString(R.string.location_area_encounters, pokemon.locationAreaEncounters)
            binding.textOrder.text = getString(R.string.order, pokemon.order)
            binding.textWeight.text = getString(R.string.weight, pokemon.weight)
        }
    }
}