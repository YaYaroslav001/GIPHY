package com.example.giphy

import retrofit2.http.GET
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.Path
import retrofit2.http.Query


interface DataService {
    @GET("gifs/trending?api_key=lGPLJ9cD7aeOZ5a9qdhZ6BSrliDoSLAo")
    fun getTrendingGifs(): retrofit2.Call<DataResult>

    @GET("gifs/search?api_key=lGPLJ9cD7aeOZ5a9qdhZ6BSrliDoSLAo&q=")
    fun getSearchGifs(): retrofit2.Call<DataResult>

    @GET
    fun getSearch(@Url url: String?): retrofit2.Call<DataResult>

    @GET("gifs/search?api_key=lGPLJ9cD7aeOZ5a9qdhZ6BSrliDoSLAo&q={search}")
    fun getUrl(@Query(value = "search", encoded = true) fullUrl: String): retrofit2.Call<DataResult>
}