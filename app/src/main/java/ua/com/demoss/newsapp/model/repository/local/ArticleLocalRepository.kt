package ua.com.demoss.newsapp.model.repository.local

import io.realm.Realm
import ua.com.demoss.newsapp.model.db.dto.RealmArticle
import ua.com.demoss.newsapp.model.repository.Repository

class ArticleLocalRepository: Repository<RealmArticle> {

    private val realm = Realm.getDefaultInstance()

    override fun create(item: RealmArticle) {
        doInTransaction { realm.copyToRealm(item) }
    }

    override fun get(): List<RealmArticle> {
        return realm.where(RealmArticle::class.java).findAll()
    }

    override fun remove(item: RealmArticle) {
        doInTransaction { item.deleteFromRealm() }
    }

    private fun doInTransaction(operation: () -> Unit){
        realm.beginTransaction()
        operation()
        realm.commitTransaction()
    }
}