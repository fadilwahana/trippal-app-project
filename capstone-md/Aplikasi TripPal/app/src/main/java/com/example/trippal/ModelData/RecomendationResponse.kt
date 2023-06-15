package com.example.trippal.ModelData

import com.google.gson.annotations.SerializedName

data class RecomendationResponse(

	@field:SerializedName("data")
	val data: List<DataItem>
)

data class DataItem(

	@field:SerializedName("name")
	val name: String
)
