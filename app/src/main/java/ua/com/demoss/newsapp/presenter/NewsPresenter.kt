package ua.com.demoss.newsapp.presenter

import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.com.demoss.newsapp.NewsApp
import ua.com.demoss.newsapp.di.component.DaggerNewsPresenterComponent
import ua.com.demoss.newsapp.di.component.NewsPresenterComponent
import ua.com.demoss.newsapp.model.api.NewsApi
import ua.com.demoss.newsapp.model.api.request.QueryMapBuilder
import ua.com.demoss.newsapp.model.api.response.ApiArticle
import ua.com.demoss.newsapp.model.api.response.ResponseObject
import ua.com.demoss.newsapp.model.db.dto.RealmArticle
import ua.com.demoss.newsapp.ui.adapter.ApiArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.ui.adapter.RealmArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.view.NewsView
import com.facebook.share.model.ShareLinkContent



@InjectViewState
class NewsPresenter: MvpPresenter<NewsView>(),
        RealmArticlesRecyclerViewAdapter.OnRealmArticleInteraction,
        ApiArticlesRecyclerViewAdapter.OnApiArticleInteraction {

    /**
     * The question is: we need two different classes for data: ApiArticle and RealmArticle
     * because classes that inherit RealmObject can't be data class (they have to have an empty constructor)
     * but for retrofit responses I use data classes. I find it very useful
     */

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
        pageNumber++
        queryBuilder.page(pageNumber)
        sendRequest()
    }

    fun sortBy(sortVariant: String){
        queryBuilder.sortBy(sortVariant)
        apiArticlesList.clear()
        sendRequest()
    }

    // Util ****************************************************************************************
    private fun sendRequest(){
        val call = newsApi.getNews(QueryMapBuilder().build())

        call.enqueue(object: Callback<ResponseObject> {

            override fun onResponse(call: Call<ResponseObject>?, response: Response<ResponseObject>?) {
                Log.i("Api", response?.toString())
                if (response?.body()?.articles != null) {
                    apiArticlesList.addAll(response.body()?.articles!!)
                    adapterApi.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseObject>?, t: Throwable?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.i("Api", call?.toString())
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
    override fun share(article: ApiArticle) {
        val content = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(article.url))
                .build()
        viewState.shareDialog(content)
    }

    override fun addToFavorites(article: ApiArticle) {
        realm.beginTransaction()
        realm.copyToRealm(RealmArticle().realmArticleFromApiArticle(article))
        realm.commitTransaction()
        adapterRealm.notifyDataSetChanged()
    }
}