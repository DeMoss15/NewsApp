package ua.com.demoss.newsapp.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ua.com.demoss.newsapp.model.api.NewsApi
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun providesNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
}