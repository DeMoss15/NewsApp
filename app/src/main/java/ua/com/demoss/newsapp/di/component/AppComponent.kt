package ua.com.demoss.newsapp.di.component

import android.app.Application
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ua.com.demoss.newsapp.di.module.ApiModule
import ua.com.demoss.newsapp.di.module.AppModule
import ua.com.demoss.newsapp.di.module.OkHttpModule
import ua.com.demoss.newsapp.di.module.RetrofitModule
import ua.com.demoss.newsapp.model.api.NewsApi
import ua.com.demoss.newsapp.model.repository.local.ArticleLocalRepository
import javax.inject.Singleton

@Component(modules = [AppModule::class,
    ApiModule::class,
    OkHttpModule::class,
    RetrofitModule::class])
@Singleton
interface AppComponent {
    fun gson(): Gson
    fun application(): Application
    fun client(): OkHttpClient
    fun retrofit(): Retrofit
    fun newsApi(): NewsApi
    fun articleLocalRepository(): ArticleLocalRepository
}