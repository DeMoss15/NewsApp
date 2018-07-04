package ua.com.demoss.newsapp.model.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestGetNewsBetweenDates(
        @SerializedName("from")
        @Expose
        val from: String,

        @SerializedName("to")
        @Expose
        val to: String,

        @SerializedName("pageSize")
        @Expose
        val pageSize: Int,

        @SerializedName("page")
        @Expose
        val page: Int
) {}