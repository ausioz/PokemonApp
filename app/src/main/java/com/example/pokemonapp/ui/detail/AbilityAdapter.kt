package com.example.pokemonapp.ui.detail


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.data.remote.response.AbilitiesItem
import com.example.pokemonapp.databinding.ItemAbilitiesBinding


class AbilityAdapter  : ListAdapter<AbilitiesItem, AbilityAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemAbilitiesBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ability: AbilitiesItem) {
            binding.tvName.text = ability.ability?.name.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAbilitiesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AbilitiesItem>() {
            override fun areItemsTheSame(
                oldItem: AbilitiesItem, newItem: AbilitiesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AbilitiesItem, newItem: AbilitiesItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}