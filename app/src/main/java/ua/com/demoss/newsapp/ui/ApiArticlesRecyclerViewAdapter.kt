package ua.com.demoss.newsapp.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.com.demoss.newsapp.model.api.response.ApiArticle

class ApiArticlesRecyclerViewAdapter(
        private val list: List<ApiArticle>,
        private val listener: OnApiArticleInteraction
): RecyclerView.Adapter<ApiArticlesRecyclerViewAdapter.NewsViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(/*TODO replace with article item layout*/android.R.layout.simple_list_item_1, parent)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.item = list[position]
        // TODO set values to views and onClickListeners
    }

    inner class NewsViewHolder(val view: View): RecyclerView.ViewHolder(view){
        lateinit var item: ApiArticle
        //TODO add view vals
    }

    interface OnApiArticleInteraction{
        fun share(article: ApiArticle)
        fun addToFavorites(article: ApiArticle)
    }
}