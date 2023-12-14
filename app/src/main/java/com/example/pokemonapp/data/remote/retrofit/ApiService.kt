package com.example.pokemonapp.data.remote.retrofit

import com.example.pokemonapp.data.remote.response.PokemonDetailResponse
import com.example.pokemonapp.data.remote.response.PokemonListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    companion object {
        private const val URL = "https://pokeapi.co/api/v2/pokemon/"
    }


    @GET(URL)
    fun getPokemonList(): Call<PokemonListResponse>


    @GET("{name}")
    fun getPokemonDetail(@Path("name") name: String?):Call<PokemonDetailResponse>

}