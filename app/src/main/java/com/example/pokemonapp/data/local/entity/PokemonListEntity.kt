package com.example.pokemonapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "pokemonlist")
data class PokemonListEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "url")
    var url: String? = null,
) : Parcelable