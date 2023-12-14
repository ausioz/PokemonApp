package com.example.pokemonapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

        mainViewModel =
            ViewModelProvider(this, ViewModelFactory(application))[MainViewModel::class.java]

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.errorMsg.observe(this) {
            showError(it)
        }
        mainViewModel.initPokemons()

        mainViewModel.listPokemons.observe(this){
            if (it == null){
                showLoading(true)
                Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show()
            } else {
                setData(it)
                Log.d("qwe", it.toString())
            }
        }

        with(binding)
        {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                val searchInput = "%${searchBar.text.toString().trim()}%"
                mainViewModel.findPokemon(searchInput)
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