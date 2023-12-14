package com.example.pokemonapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonapp.R
import com.example.pokemonapp.data.remote.response.ResultsItem
import com.example.pokemonapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        mainViewModel =
            ViewModelProvider(this, ViewModelFactory(application))[MainViewModel::class.java]

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.errorMsg.observe(this) {
            showError(it)
        }

        mainViewModel.listPokemons.observe(this) {
            setData(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()

                val searchInput = "%${searchBar.text.toString().trim()}%"
                mainViewModel.searchPokemon(searchInput)
                if (searchBar.text.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity, "Kata kunci harus diisi", Toast.LENGTH_SHORT
                    ).show()
                }
                false
            }
        }



        binding.recyclerViewMain.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        binding.toolbar.inflateMenu(R.menu.option_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.ascending -> {
                    mainViewModel.sortAscending(binding.searchBar.text.toString())
                    true
                }
                R.id.descending -> {
                    mainViewModel.sortDescending(binding.searchBar.text.toString())
                    true
                }
                else -> false
            }
        }


        return super.onCreateOptionsMenu(menu)
    }

    private fun setData(list: List<ResultsItem>) {
        val pokemonListAdapter = PokemonListAdapter()
        pokemonListAdapter.submitList(list)
        binding.recyclerViewMain.adapter = pokemonListAdapter
    }


    private fun showError(errorMsg: String) {
        Toast.makeText(this, "Error! \n$errorMsg", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerViewMain.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerViewMain.visibility = View.VISIBLE
        }
    }
}