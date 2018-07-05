package ua.com.demoss.newsapp.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.activity_main.*
import ua.com.demoss.newsapp.R
import ua.com.demoss.newsapp.presenter.NewsPresenter
import ua.com.demoss.newsapp.ui.adapter.ApiArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.ui.adapter.RealmArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.view.NewsView

class MainActivity : MvpAppCompatActivity(), NewsView {

    // Variables ***********************************************************************************
    @InjectPresenter
    lateinit var presenter: NewsPresenter

    // Activity ************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.onCreate()

        activity_main_button_favorites.setOnClickListener{presenter.switchToFavorites()}
        activity_main_button_api.setOnClickListener { presenter.switchToApi() }

        activity_main_recycler_view_articles.layoutManager = LinearLayoutManager(this)
        // setOnScrollListener is deprecated ?!
        activity_main_recycler_view_articles.setOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0){// Recycle view scrolling downwards...

                    if (!recyclerView?.canScrollVertically(RecyclerView.FOCUS_DOWN)!!){// can't scroll more
                        presenter.getNextPage()
                    }
                }
            }
        })
    }

    // NewsView ************************************************************************************
    override fun setAdapter(adapter: RealmArticlesRecyclerViewAdapter) {
        activity_main_recycler_view_articles.adapter = adapter
    }

    override fun setAdapter(adapter: ApiArticlesRecyclerViewAdapter) {
        activity_main_recycler_view_articles.adapter = adapter
    }

    override fun shareDialog(content: ShareLinkContent) {
        ShareDialog.show(this, content)
    }
}
