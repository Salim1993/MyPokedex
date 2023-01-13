package com.salim.mypokedex.pokemonDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.salim.mypokedex.R
import com.salim.mypokedex.databinding.FragmentPokemonDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PokemonDetailsFragment : Fragment(R.layout.fragment_pokemon_details) {

    @Inject lateinit var getPokemonDetailsUseCaseFactory: GetPokemonDetailsUseCaseFactory

    private val args: PokemonDetailsFragmentArgs by navArgs()

    private val viewModel: PokemonDetailsViewModel by viewModels {
        PokemonDetailsViewModelFactory(args.pokemonName, getPokemonDetailsUseCaseFactory)
    }
    private lateinit var binding: FragmentPokemonDetailsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)

        binding = FragmentPokemonDetailsBinding.bind(view)

        viewModel.pokemonDetailsFlow.asLiveData().observe(viewLifecycleOwner) {
            it?.let { pokemon ->
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
}