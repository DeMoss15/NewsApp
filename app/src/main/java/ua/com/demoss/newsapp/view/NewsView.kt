package ua.com.demoss.newsapp.view

import com.arellomobile.mvp.MvpView
import ua.com.demoss.newsapp.ui.ApiArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.ui.RealmArticlesRecyclerViewAdapter

interface NewsView: MvpView {

    fun setAdapter(adapter: RealmArticlesRecyclerViewAdapter)
    fun setAdapter(adapter: ApiArticlesRecyclerViewAdapter)
}