package ua.com.demoss.newsapp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.realm.Realm
import retrofit2.Call
import ua.com.demoss.newsapp.NewsApp
import ua.com.demoss.newsapp.di.component.DaggerNewsPresenterComponent
import ua.com.demoss.newsapp.di.component.NewsPresenterComponent
import ua.com.demoss.newsapp.model.api.NewsApi
import ua.com.demoss.newsapp.model.api.response.ApiArticle
import ua.com.demoss.newsapp.model.api.response.ResponseObject
import ua.com.demoss.newsapp.model.db.dto.RealmArticle
import ua.com.demoss.newsapp.ui.ApiArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.ui.RealmArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.view.NewsView

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

    private var sortBy = ""
    private var filterBy = ""
    private var pageNumber: Int = 1

    private var component: NewsPresenterComponent = DaggerNewsPresenterComponent.builder().appComponent(NewsApp.appComponent).build()
    private var newsApi: NewsApi = component.newsApi()

    // View events *********************************************************************************
    fun onCreate(){
        getPage()
        viewState.setAdapter(adapterApi)
    }

    fun getFavorites(){
        viewState.setAdapter(adapterRealm)
    }

    fun getFromApi(){
        getPage()
        viewState.setAdapter(adapterApi)
    }

    fun getNextPage(){
        pageNumber ++
        getPage()
    }

    fun setFilter(filter: String){
        filterBy = filter
        pageNumber = 1
        getPage()
    }

    fun setSorted(sortBy: String){
        this.sortBy = sortBy
        pageNumber = 1
        getPage()
    }

    // Util ****************************************************************************************
    private fun getPage(){
        lateinit var call: Call<ResponseObject>

        when{
            !sortBy.isBlank() && !filterBy.isBlank() -> {// page of sorted and filtered news

            }
            !sortBy.isBlank() && filterBy.isBlank() -> {// page of sorted news

            }
            sortBy.isBlank() && !filterBy.isBlank() -> {// page of filtered news

            }
            sortBy.isBlank() && filterBy.isBlank() -> {// not sorted and not filtered news

            }
        }
        // enqueue call
        adapterApi.notifyDataSetChanged()
    }

    // OnRealmArticleInteraction *******************************************************************
    override fun share(article: RealmArticle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFromFavorites(article: RealmArticle) {
        realm.beginTransaction()
        realmArticlesList.deleteFromRealm(realmArticlesList.indexOf(article))
        realm.commitTransaction()
        adapterRealm.notifyDataSetChanged()
    }

    // OnApiArticleInteraction *********************************************************************
    override fun share(article: ApiArticle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addToFavorites(article: ApiArticle) {
        realm.beginTransaction()
        realm.copyToRealm(RealmArticle().realmArticleFromApiArticle(article))
        realm.commitTransaction()
        adapterRealm.notifyDataSetChanged()
    }
}