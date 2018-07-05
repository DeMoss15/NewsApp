package ua.com.demoss.newsapp.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import ua.com.demoss.newsapp.R
import ua.com.demoss.newsapp.presenter.NewsPresenter
import ua.com.demoss.newsapp.view.NewsView

class MainActivity : MvpAppCompatActivity(), NewsView {

    @InjectPresenter
    lateinit var presenter: NewsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.onCreate()
        activity_main_recycler_view_articles.layoutManager = LinearLayoutManager(this)
        activity_main_button_favorites.setOnClickListener{presenter.switchToFavorites()}
        activity_main_button_api.setOnClickListener { presenter.switchToApi() }
    }

    // NewsView ************************************************************************************
    override fun setAdapter(adapter: RealmArticlesRecyclerViewAdapter) {
        activity_main_recycler_view_articles.adapter = adapter
    }

    override fun setAdapter(adapter: ApiArticlesRecyclerViewAdapter) {
        activity_main_recycler_view_articles.adapter = adapter
    }
}
