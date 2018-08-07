package ua.com.demoss.newsapp.di.component

import dagger.Component
import ua.com.demoss.newsapp.di.PresenterScope
import ua.com.demoss.newsapp.di.module.NewsPresenterModule
import ua.com.demoss.newsapp.model.api.NewsApi
import ua.com.demoss.newsapp.model.repository.local.ArticleLocalRepository

@PresenterScope
@Component(dependencies = [AppComponent::class],
        modules = [NewsPresenterModule::class])
interface NewsPresenterComponent {

    fun newsApi(): NewsApi
    fun localRepository(): ArticleLocalRepository
}