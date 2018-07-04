package ua.com.demoss.newsapp.model.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestGetNews(
        @SerializedName("pageSize")
        @Expose
        val pageSize: Int,

        @SerializedName("page")
        @Expose
        val page: Int
) {}