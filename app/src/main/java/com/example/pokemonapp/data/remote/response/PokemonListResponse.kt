package com.example.pokemonapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PokemonListResponse(

	@field:SerializedName("next")
	val next: String,

	@field:SerializedName("previous")
	val previous: Any,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("results")
	val results: List<ResultsItem>
)

@Parcelize
data class ResultsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) :Parcelable
