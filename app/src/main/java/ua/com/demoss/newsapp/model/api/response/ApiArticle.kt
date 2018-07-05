package ua.com.demoss.newsapp.model.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiArticle(
        @SerializedName("source")
        @Expose
        val source: ApiSource?,

        @SerializedName("author")
        @Expose
        val author: String?,

        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("description")
        @Expose
        val description: String?,

        @SerializedName("url")
        @Expose
        val url: String?,

        @SerializedName("urlToImage")
        @Expose
        val urlToImage: String?,

        @SerializedName("publishedAt")
        @Expose
        val publishedAt: String?
) {}