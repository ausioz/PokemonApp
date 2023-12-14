package com.example.pokemonapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonapp.data.local.entity.PokemonListEntity
import com.example.pokemonapp.data.remote.response.ResultsItem

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(pokemon: PokemonListEntity)

    @Delete
    fun deleteList(pokemon: PokemonListEntity)

    @Query("SELECT * FROM pokemonlist")
    fun getList(): List<PokemonListEntity>

    @Query("SELECT * FROM pokemonlist WHERE name LIKE :name" )
    fun searchPokemon(name: String): List<ResultsItem>

    @Query("SELECT * FROM pokemonlist WHERE name LIKE :name ORDER BY name ASC " )
    fun getPokemonListAscending(name: String): List<ResultsItem>

    @Query("SELECT * FROM pokemonlist WHERE name LIKE :name ORDER BY name DESC" )
    fun getPokemonListDescending(name: String): List<ResultsItem>

}