package com.salim.mypokedex.pokemonList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.salim.mypokedex.R
import com.salim.mypokedex.databinding.PokemonListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListFragment : Fragment(R.layout.pokemon_list_fragment) {

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var binding: PokemonListFragmentBinding
    private lateinit var pokemonListAdapter: PokemonListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = PokemonListFragmentBinding.bind(view)
        setupViews()
        setupObservables()
    }

    private fun setupViews() {
        pokemonListAdapter = PokemonListAdapter(viewModel.pokemonList.value)

        with(binding.pokemonList) {
            adapter = pokemonListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun setupObservables() {
        viewModel.pokemonList.asLiveData().observe(viewLifecycleOwner) {
            pokemonListAdapter.pokemonList = it
            pokemonListAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance() = PokemonListFragment()
    }
}