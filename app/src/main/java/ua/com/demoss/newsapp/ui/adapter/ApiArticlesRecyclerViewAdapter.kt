package ua.com.demoss.newsapp.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_article.view.*
import ua.com.demoss.newsapp.R
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
                .inflate(R.layout.item_article, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.item = list[position]
        holder.textViewName.text = holder.item.title
        Picasso.get().load(holder.item.urlToImage).into(holder.imageView)
        holder.imageButton.setOnClickListener { listener.addToFavorites(holder.item) }
        holder.view.setOnClickListener { listener.share(holder.item) }
    }

    inner class NewsViewHolder(val view: View): RecyclerView.ViewHolder(view){
        lateinit var item: ApiArticle
        val textViewName = view.item_article_text_view_title!!
        val imageView = view.item_article_image_view!!
        val imageButton = view.item_article_image_button_download!!
    }

    interface OnApiArticleInteraction{
        fun share(article: ApiArticle)
        fun addToFavorites(article: ApiArticle)
    }
}