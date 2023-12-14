package com.example.pokemonapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokemonapp.data.local.entity.PokemonListEntity

@Database(entities = [PokemonListEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var instance: PokemonDatabase? = null
        fun getInstance(context: Context): PokemonDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext, PokemonDatabase::class.java, "pokemonlist.db"
            ).build()
        }
    }
}