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

    private var _listPokemons = MutableLiveData<List<ResultsItem>>()
    var listPokemons: LiveData<List<ResultsItem>> = _listPokemons

    private var _listQuery = MutableLiveData<List<PokemonListEntity>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg


    fun findPokemon(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _listQuery.postValue(db.pokemonDao().searchPokemon(name))
            _listQuery.value?.forEach {
                _listPokemons.postValue(listOf(ResultsItem(it.name, it.url)))
            }
        }
        Log.d("asd", _listPokemons.value.toString())
    }



    fun initPokemons() {
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