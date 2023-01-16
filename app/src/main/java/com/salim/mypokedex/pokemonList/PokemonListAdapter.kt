package com.salim.mypokedex.pokemonList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.salim.mypokedex.databinding.PokemonListItemBinding
import com.salim.mypokedex.pokemon.PokemonNameAndId
import java.util.ArrayList

class PokemonListAdapter(private var pokemonList: List<PokemonNameAndId>, private val onClick: (String) -> Unit)
    : RecyclerView.Adapter<PokemonListAdapter.PokemonItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonItemViewHolder {
        val binding = PokemonListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PokemonItemViewHolder, position: Int) {
        val pokemonNameAndId = pokemonList[position]
        with(holder.binding) {
            pokemonNumberText.text = "#${pokemonNameAndId.id}"

            pokemonNameText.text = pokemonNameAndId.name

            root.setOnClickListener { onClick(pokemonNameAndId.name) }
        }
    }

    override fun getItemCount(): Int = pokemonList.size

    fun submitNewList(newList: List<PokemonNameAndId>) {
        val diff = DiffUtil.calculateDiff(PokemonListDiffUtilCallback(pokemonList, newList))

        pokemonList = newList
        diff.dispatchUpdatesTo(this)
    }

    inner class PokemonItemViewHolder(val binding: PokemonListItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    class PokemonListDiffUtilCallback(private val oldList: List<PokemonNameAndId>, private val newList: List<PokemonNameAndId>): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemPosition == newItemPosition
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}