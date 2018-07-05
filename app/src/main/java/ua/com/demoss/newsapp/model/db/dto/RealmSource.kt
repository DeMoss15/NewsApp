package ua.com.demoss.newsapp.model.db.dto

import io.realm.RealmObject
import ua.com.demoss.newsapp.model.api.response.ApiSource

open class RealmSource: RealmObject(){
    var id: String? = null
    var name: String? = null

    fun realmSourceFromApiSource(source: ApiSource?): RealmSource{
        id = source?.id
        name = source?.name
        return this
    }
}