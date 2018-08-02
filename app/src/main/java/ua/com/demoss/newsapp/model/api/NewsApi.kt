package ua.com.demoss.newsapp.model.api

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import ua.com.demoss.newsapp.model.api.response.ResponseObject

interface NewsApi {

    @GET("/v2/everything")
    fun getNews(@QueryMap options: Map<String, String>): Observable<Response<ResponseObject>>
}