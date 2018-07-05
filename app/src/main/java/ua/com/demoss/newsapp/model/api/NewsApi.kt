package ua.com.demoss.newsapp.model.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import ua.com.demoss.newsapp.model.api.response.ResponseObject

interface NewsApi {

    @GET("/v2/everything")
    fun getNews(@QueryMap options: Map<String, String>): Call<ResponseObject>

    @GET("/v2/everything")
    fun getTopNews(@QueryMap options: Map<String, String>): Call<ResponseObject>
}