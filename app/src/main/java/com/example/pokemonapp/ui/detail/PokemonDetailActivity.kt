package com.example.pokemonapp.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pokemonapp.data.remote.response.AbilitiesItem
import com.example.pokemonapp.data.remote.response.ResultsItem
import com.example.pokemonapp.databinding.ActivityPokemonDetailBinding
import com.example.pokemonapp.ui.PokemonListAdapter

class PokemonDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewDetail.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        val parcel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PokemonListAdapter.EXTRA_POKEMON, ResultsItem::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getParcelableExtra(PokemonListAdapter.EXTRA_POKEMON) as? ResultsItem
        }
        val name = parcel?.name.toString()
//        val url = parcel?.url.toString()

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        detailViewModel.getPokemonDetail(name)

        detailViewModel.errorMsg.observe(this){
            showError(it)
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        detailViewModel.pokemonDetail.observe(this){
            binding.tvName.text = it.name
            Glide.with(this).load(it.sprites?.frontDefault).into(binding.ivImage)

            setData(it.abilities)
        }

    }

    private fun setData(list: List<AbilitiesItem?>?) {
        val abilityAdapter = AbilityAdapter()
        abilityAdapter.submitList(list)
        binding.recyclerViewDetail.adapter = abilityAdapter
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(this, "Error! \n$errorMsg", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.recyclerViewDetail.visibility = View.GONE
        } else {
            binding.recyclerViewDetail.visibility = View.VISIBLE
        }
    }
}