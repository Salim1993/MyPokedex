package com.salim.mypokedex.pokemonList

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.salim.mypokedex.R
import com.salim.mypokedex.databinding.PokemonListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first


@AndroidEntryPoint
class PokemonListFragment : Fragment(R.layout.pokemon_list_fragment) {

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var binding: PokemonListFragmentBinding
    private lateinit var pokemonListAdapter: PokemonListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = PokemonListFragmentBinding.bind(view)
        setupViews()
        setupSearchMenu()
        setupObservables()
    }

    private fun setupViews() {
        pokemonListAdapter = PokemonListAdapter(
            viewModel.pokemonList.value,
            this::clickedPokemon
        )

        with(binding.pokemonList) {
            adapter = pokemonListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun setupObservables() {
        viewModel.pokemonList.asLiveData().observe(viewLifecycleOwner) {
            pokemonListAdapter.submitNewList(it)
        }
    }

    private fun clickedPokemon(pokemonName: String) {
        val action = PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(pokemonName)
        findNavController().navigate(action)
    }

    private fun setupSearchMenu() {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_pokemon_list, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem!!.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        /* If user cleared the search then set the complete list on RV */
                        return true
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        if(query.isNullOrEmpty()){
                            pokemonListAdapter.submitNewList(ArrayList(viewModel.pokemonList.value))
                        }
                        else {
                            val filteredList = viewModel.pokemonList.value.filter { it.name.startsWith(query) }
                            pokemonListAdapter.submitNewList(ArrayList(filteredList))
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}