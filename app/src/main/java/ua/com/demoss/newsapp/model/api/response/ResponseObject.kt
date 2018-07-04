package ua.com.demoss.newsapp.model.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseObject(
        @SerializedName("status")
        @Expose
        val status: String,
        @SerializedName("totalResults")
        @Expose
        val totalResults: Int,
        @SerializedName("articles")
        @Expose
        val articles: ArrayList<ApiArticle>
) {}