package com.example.pokemonapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.data.remote.response.ResultsItem
import com.example.pokemonapp.databinding.ItemPokemonBinding
import com.example.pokemonapp.ui.detail.PokemonDetailActivity

class PokemonListAdapter : ListAdapter<ResultsItem, PokemonListAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemPokemonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: ResultsItem) {
            binding.tvName.text = pokemon.name

            binding.cardUser.setOnClickListener {
                setListeners(getItem(adapterPosition), holder = ViewHolder(binding))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun setListeners(selectedUser: ResultsItem, holder: ViewHolder) {
        val intent = Intent(holder.itemView.context, PokemonDetailActivity::class.java)
        // pass record to next activity
        intent.putExtra(EXTRA_POKEMON, selectedUser)
        holder.itemView.context.startActivity(intent)
    }

    companion object {
        const val EXTRA_POKEMON = "pokemon"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(
                oldItem: ResultsItem, newItem: ResultsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ResultsItem, newItem: ResultsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}