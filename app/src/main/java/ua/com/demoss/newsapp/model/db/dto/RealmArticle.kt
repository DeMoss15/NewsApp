package ua.com.demoss.newsapp.model.db.dto

import io.realm.RealmObject
import ua.com.demoss.newsapp.model.api.response.ApiArticle
import ua.com.demoss.newsapp.model.entity.Article

open class RealmArticle: RealmObject(), Article{
    var source: RealmSource? = null
    var author: String? = null
    var title: String? = null
    var description: String? = null
    var url: String? = null
    var urlToImage: String? = null
    var publishedAt: String? = null

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

    override fun getName(): String {
        return title!!
    }

    override fun getImage(): String {
        return urlToImage!!
    }
}