package ua.com.demoss.newsapp.model.db.dto

import io.realm.RealmObject
import ua.com.demoss.newsapp.model.api.response.ApiArticle

class RealmArticle: RealmObject() {
    lateinit var source: RealmSource
    lateinit var author: String
    lateinit var title: String
    lateinit var description: String
    lateinit var url: String
    lateinit var urlToImage: String
    lateinit var publishedAt: String

    fun realmArticleFromApiArticle(article: ApiArticle): RealmArticle{
        source = RealmSource().realmSourceFromApiSource(article.source)
        author = article.author
        title = article.title
        description = article.description
        url = article.url
        urlToImage = article.urlToImage
        publishedAt = article.publishedAt

        return this
    }
}