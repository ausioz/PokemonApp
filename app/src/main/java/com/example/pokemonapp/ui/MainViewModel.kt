package com.example.pokemonapp.ui


import android.app.Application
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.local.entity.PokemonListEntity
import com.example.pokemonapp.data.local.room.PokemonDatabase
import com.example.pokemonapp.data.remote.response.PokemonListResponse
import com.example.pokemonapp.data.remote.response.ResultsItem
import com.example.pokemonapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : ViewModel() {

    val db: PokemonDatabase = PokemonDatabase.getInstance(application)

    private val _listPokemons = MutableLiveData<List<ResultsItem>>()
    val listPokemons: LiveData<List<ResultsItem>> = _listPokemons

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    init {
        initPokemons()
    }

    fun searchPokemon(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _listPokemons.postValue(db.pokemonDao().searchPokemon(name))
            Log.d("asd", "${db.pokemonDao().searchPokemon(name)}")
        }
    }

    fun sortAscending(name:String){
        searchPokemon(name)
        viewModelScope.launch(Dispatchers.IO) {
            _listPokemons.postValue(db.pokemonDao().getPokemonListAscending(name))
        }
    }

    fun sortDescending(name:String){
        searchPokemon(name)
        viewModelScope.launch(Dispatchers.IO) {
            _listPokemons.postValue(db.pokemonDao().getPokemonListDescending(name))
        }
    }

    private fun initPokemons() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getPokemonList()
        client.enqueue(object : Callback<PokemonListResponse> {
            override fun onResponse(
                call: Call<PokemonListResponse>, response: Response<PokemonListResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _listPokemons.value = response.body()?.results
                        viewModelScope.launch(Dispatchers.IO) {
                            _listPokemons.value?.forEach { pokemon ->
                                db.pokemonDao()
                                    .insertList(PokemonListEntity(pokemon.name!!, pokemon.url))
                            }
                        }
                    }
                } else {
                    _isLoading.value = false
                    _errorMsg.value = response.message()
                    Log.e("MainViewModel", "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
                _errorMsg.value = t.message
            }

        })
    }

}