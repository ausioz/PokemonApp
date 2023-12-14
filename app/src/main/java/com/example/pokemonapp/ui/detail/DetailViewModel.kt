package com.example.pokemonapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.local.entity.PokemonListEntity
import com.example.pokemonapp.data.remote.response.PokemonDetailResponse
import com.example.pokemonapp.data.remote.response.PokemonListResponse
import com.example.pokemonapp.data.remote.response.ResultsItem
import com.example.pokemonapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel:ViewModel() {
    private val _pokemonDetail = MutableLiveData<PokemonDetailResponse>()
    val pokemonDetail: LiveData<PokemonDetailResponse> = _pokemonDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg


    fun getPokemonDetail(name:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getPokemonDetail(name)
        client.enqueue(object : Callback<PokemonDetailResponse> {
            override fun onResponse(
                call: Call<PokemonDetailResponse>,
                response: Response<PokemonDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _pokemonDetail.value = response.body()
                    }
                } else {
                    _isLoading.value = false
                    _errorMsg.value = response.message()
                    Log.e("MainViewModel", "onFailure: ${response.message()}")
                }

            }
            override fun onFailure(call: Call<PokemonDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
                _errorMsg.value = t.message
            }

        })
    }
}