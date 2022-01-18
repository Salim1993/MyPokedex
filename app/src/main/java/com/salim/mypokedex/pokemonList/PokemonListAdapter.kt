package com.salim.mypokedex.pokemonList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salim.mypokedex.databinding.PokemonListItemBinding

class PokemonListAdapter(var pokemonList: List<String>)
    : RecyclerView.Adapter<PokemonListAdapter.PokemonItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonItemViewHolder {
        val binding = PokemonListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PokemonItemViewHolder, position: Int) {
        val pokemonName = pokemonList[position]
        with(holder.binding) {
            pokemonNumberText.text = "#${position + 1}"

            pokemonNameText.text = pokemonName
        }
    }

    override fun getItemCount(): Int = pokemonList.size

    inner class PokemonItemViewHolder(val binding: PokemonListItemBinding)
        :RecyclerView.ViewHolder(binding.root)
}