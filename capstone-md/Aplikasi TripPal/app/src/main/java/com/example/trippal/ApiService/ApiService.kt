package com.example.trippal.ApiService

import com.example.trippal.ModelData.DataItem
import com.example.trippal.ModelData.RecomendationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

@POST("recommendations")
fun recommendations(): Call<RecomendationResponse>

}