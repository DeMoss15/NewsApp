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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.com.demoss.newsapp.model.api.response.ResponseObject
import java.util.*


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

    // View events *********************************************************************************
    fun onCreate(){
        viewState.setAdapter(adapterApi)
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
        val result = newsApi.getNews(queryBuilder.build())
        result.enqueue(object: Callback<ResponseObject> {

            override fun onResponse(call: Call<ResponseObject>?, response: Response<ResponseObject>?) {
                Log.i("Api", response?.toString())

                when { // Handling HTTP response code
                    response?.code().toString().startsWith("1") ->{
                        viewState.showToast("informational ${response?.code().toString()}")
                    }
                    response?.code().toString().startsWith("2") ->{
                        viewState.showToast("success ${response?.code().toString()}")
                        onReceiveResponse(response!!)
                    }
                    response?.code().toString().startsWith("3") ->{
                        viewState.showToast("redirection ${response?.code().toString()}")
                    }
                    response?.code().toString().startsWith("4") ->{
                        viewState.showToast("client error ${response?.code().toString()}")
                    }
                    response?.code().toString().startsWith("5") ->{
                        viewState.showToast("server error ${response?.code().toString()}")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseObject>?, t: Throwable?) {
                Log.e("Api", call?.toString())
            }
        })
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

    fun onReceiveResponse(response: Response<ResponseObject>) {
        if (pageFlag) {
            pageFlag = false
        } else {
            apiArticlesList.clear()
        }

        try {
            apiArticlesList.addAll(response.body()?.articles!!)
            adapterApi.notifyDataSetChanged()
        }
        catch (e: NullPointerException){
            Log.i("presenter", "response.body()?.articles!! is null \n${e.toString()}")
            viewState.showToast("Ooops! Something went wrong...")
        }

    }
}