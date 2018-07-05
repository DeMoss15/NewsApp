package ua.com.demoss.newsapp.model.db.dto

import io.realm.RealmObject
import ua.com.demoss.newsapp.model.api.response.ApiSource

class RealmSource: RealmObject(){
    var id: String? = null
    lateinit var name: String

    fun realmSourceFromApiSource(source: ApiSource): RealmSource{
        id = source.id
        name = source.name
        return this
    }
}