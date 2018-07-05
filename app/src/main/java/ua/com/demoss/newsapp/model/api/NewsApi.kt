package ua.com.demoss.newsapp.model.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import ua.com.demoss.newsapp.model.api.request.RequestGetNews
import ua.com.demoss.newsapp.model.api.request.RequestGetNewsBetweenDates
import ua.com.demoss.newsapp.model.api.request.RequestGetNewsBetweenDatesSortedBy
import ua.com.demoss.newsapp.model.api.request.RequestGetNewsSortedBy
import ua.com.demoss.newsapp.model.api.response.ResponseObject
import java.text.SimpleDateFormat
import java.util.*

interface NewsApi {

    companion object {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        const val SORT_BY_RELEVANCY = "relevancy"
        const val SORT_BY_POPULARITY = "popularity"
        const val PAGE_SIZE: Int = 20 //There is no literal for Int, only for Long
    }

    @GET("/v2/everything")
    fun getNews(
            @Query("X-Authorization") apiKey: String,
            @Body request: RequestGetNews): Call<ResponseObject>

    @GET("/v2/everything")
    fun getNewsBetweenDates(
            @Query("X-Authorization") apiKey: String,
            @Body request: RequestGetNewsBetweenDates): Call<ResponseObject>

    @GET("/v2/everything")
    fun getNewsSortedBy(
            @Query("X-Authorization") apiKey: String,
            @Body request: RequestGetNewsSortedBy): Call<ResponseObject>

    @GET("/v2/everything")
    fun getNewsBetweenDatesSortedBy(
            @Query("X-Authorization") apiKey: String,
            @Body request: RequestGetNewsBetweenDatesSortedBy): Call<ResponseObject>
}