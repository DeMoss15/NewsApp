package ua.com.demoss.newsapp.presenter

import android.net.Uri
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.realm.Realm
import ua.com.demoss.newsapp.NewsApp
import ua.com.demoss.newsapp.di.component.DaggerNewsPresenterComponent
import ua.com.demoss.newsapp.di.component.NewsPresenterComponent
import ua.com.demoss.newsapp.model.api.NewsApi
import ua.com.demoss.newsapp.model.api.request.QueryMapBuilder
import ua.com.demoss.newsapp.model.api.response.ApiArticle
import ua.com.demoss.newsapp.model.db.dto.RealmArticle
import ua.com.demoss.newsapp.ui.adapter.ApiArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.ui.adapter.RealmArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.view.NewsView
import com.facebook.share.model.ShareLinkContent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ua.com.demoss.newsapp.model.api.response.ResponseObject
import java.util.*
import android.net.ConnectivityManager


@InjectViewState
class NewsPresenter: MvpPresenter<NewsView>(),
        RealmArticlesRecyclerViewAdapter.OnRealmArticleInteraction,
        ApiArticlesRecyclerViewAdapter.OnApiArticleInteraction {

    // Variables ***********************************************************************************
    private val realm = Realm.getDefaultInstance()
    private val realmArticlesList = realm.where(RealmArticle::class.java).findAll()
    private val adapterRealm = RealmArticlesRecyclerViewAdapter(realmArticlesList, this)

    private val apiArticlesList = ArrayList<ApiArticle>()
    private val adapterApi = ApiArticlesRecyclerViewAdapter(apiArticlesList, this)

    private var pageNumber: Int = 1

    private var component: NewsPresenterComponent = DaggerNewsPresenterComponent.builder().appComponent(NewsApp.appComponent).build()
    private var newsApi: NewsApi = component.newsApi()
    private var queryBuilder = QueryMapBuilder()

    lateinit var connectivityManager: ConnectivityManager
    // View events *********************************************************************************
    fun onCreate(connectivityManager: ConnectivityManager){
        viewState.setAdapter(adapterApi)
        this.connectivityManager = connectivityManager
        sendRequest()
    }

    fun switchToFavorites(){
        viewState.setAdapter(adapterRealm)
    }

    fun switchToApi(){
        viewState.setAdapter(adapterApi)
    }

    /*
    * TODO Change these methods according to UX ...
    * */
    fun setQuery(text: String){
        queryBuilder.query(text)
        sendRequest()
    }

    fun getNextPage(){
        pageFlag = true
        pageNumber++
        queryBuilder.page(pageNumber)
        sendRequest()
    }

    fun setFrom(date: Calendar){
        queryBuilder.from(date)
        sendRequest()
    }

    fun setTo(date: Calendar){
        queryBuilder.to(date)
        sendRequest()
    }

    fun sortBy(sortVariant: String){
        queryBuilder.sortBy(sortVariant)
        apiArticlesList.clear()
        sendRequest()
    }

    // Util ****************************************************************************************
    private fun sendRequest(){
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnectedOrConnecting) {
            newsApi.getNews(queryBuilder.build())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { e ->
                        Log.e("presenter", e.toString())
                    }
                    .subscribe { response ->
                        Log.i("presenter", "doOnNext")
                        val code = response.code().toString()
                        when { // Handling HTTP response code
                            code.startsWith("1") -> {
                                viewState.showToast("informational $code")
                            }
                            code.startsWith("2") -> {
                                viewState.showToast("success $code")
                                onReceiveResponse(response!!)
                            }
                            code.startsWith("3") -> {
                                viewState.showToast("redirection $code")
                            }
                            code.startsWith("4") -> {
                                viewState.showToast("client error $code")
                            }
                            code.startsWith("5") -> {
                                viewState.showToast("server error $code")
                            }
                        }
                    }
        }
    }
    // OnRealmArticleInteraction *******************************************************************
    override fun removeFromFavorites(article: RealmArticle) {
        realm.beginTransaction()
        realmArticlesList.deleteFromRealm(realmArticlesList.indexOf(article))
        realm.commitTransaction()
        adapterRealm.notifyDataSetChanged()
    }

    // OnApiArticleInteraction *********************************************************************
    override fun onShare(article: ApiArticle) {
        val content = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(article.url))
                .build()
        viewState.shareDialog(content)
    }

    override fun onDownload(article: ApiArticle) {
        realm.beginTransaction()
        realm.copyToRealm(RealmArticle().realmArticleFromApiArticle(article))
        realm.commitTransaction()
        adapterRealm.notifyDataSetChanged()
    }

    override fun onItemClick(article: ApiArticle) {
        article.url?.let { viewState.openUri(it) }
    }

    // Util ****************************************************************************************
    // Yeah, I know flags isn't good but I don't know another way ... =(
    var pageFlag = false

    private fun onReceiveResponse(response: Response<ResponseObject>) {
        if (pageFlag) {
            pageFlag = false
        } else {
            apiArticlesList.clear()
        }

        Log.i("presenter", "onReceiveResponse")
        try {
            apiArticlesList.addAll(response.body()?.articles!!)
            adapterApi.notifyDataSetChanged()
        }
        catch (e: NullPointerException){
            Log.e("presenter", "response.body()?.articles!! is null \n${e.toString()}")
            viewState.showToast("Ooops! Something went wrong...")
        }

    }
}