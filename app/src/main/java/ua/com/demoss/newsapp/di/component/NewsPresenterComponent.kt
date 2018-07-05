package ua.com.demoss.newsapp.di.component

import dagger.Component
import ua.com.demoss.newsapp.di.PresenterScope
import ua.com.demoss.newsapp.di.module.NewsPresenterModule
import ua.com.demoss.newsapp.model.api.NewsApi

@PresenterScope
@Component(dependencies = [AppComponent::class],
        modules = [NewsPresenterModule::class])
interface NewsPresenterComponent {

    fun newsApi(): NewsApi
}