package ua.com.demoss.newsapp.view

import com.arellomobile.mvp.MvpView
import com.facebook.share.model.ShareLinkContent
import ua.com.demoss.newsapp.ui.adapter.ApiArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.ui.adapter.RealmArticlesRecyclerViewAdapter

interface NewsView: MvpView {

    fun setAdapter(adapter: RealmArticlesRecyclerViewAdapter)
    fun setAdapter(adapter: ApiArticlesRecyclerViewAdapter)
    fun shareDialog(content: ShareLinkContent)
    fun showToast(text: String)
}