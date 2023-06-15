package com.example.trippal.MainUi.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trippal.ApiService.ApiConfig
import com.example.trippal.ModelData.DataItem
import com.example.trippal.ModelData.RecomendationResponse
import retrofit2.Call
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val recommendation = MutableLiveData<List<DataItem>>()

    fun loadRecommendation(){
        val client = ApiConfig.getApiService().recommendations()
        client.enqueue(object : retrofit2.Callback<RecomendationResponse>{
            override fun onResponse(call: Call<RecomendationResponse>, response: Response<RecomendationResponse>) {
                if (response.isSuccessful){
                    val recUser = response.body()
                    if (recUser != null){
                        recommendation.postValue(recUser.data)
                    }
                }

                else{
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecomendationResponse>, t: Throwable) {
                Log.e(TAG,"onFailure: ${t.message}")
            }

        })
    }

    fun getRecommendation(): LiveData<List<DataItem>>{
        return recommendation
    }

    companion object{
        const val TAG="Home View Model"
    }


}