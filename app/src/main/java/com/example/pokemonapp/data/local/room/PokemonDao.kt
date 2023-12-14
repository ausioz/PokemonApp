package com.example.pokemonapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonapp.data.local.entity.PokemonListEntity

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(pokemon: PokemonListEntity)

    @Delete
    fun deleteList(pokemon: PokemonListEntity)

    @Query("SELECT * FROM pokemonlist")
    fun getList(): List<PokemonListEntity>

    @Query("SELECT * FROM pokemonlist WHERE name LIKE :name" )
    fun searchPokemon(name: String): List<PokemonListEntity>

    @Query("SELECT * FROM pokemonlist ORDER BY name = :name ASC" )
    fun getPokemonListAscending(name: String): LiveData<PokemonListEntity>

    @Query("SELECT * FROM pokemonlist ORDER BY name = :name DESC" )
    fun getPokemonListDescending(name: String): LiveData<PokemonListEntity>

}