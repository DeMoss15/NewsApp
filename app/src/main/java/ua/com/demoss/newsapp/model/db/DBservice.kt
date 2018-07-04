package ua.com.demoss.newsapp.model.db

import io.realm.Realm
import io.realm.RealmObject

class DBservice {

    val realm = Realm.getDefaultInstance()

    fun <T: RealmObject>copyToRealm(obj: T): T{
        realm.beginTransaction()
        val t = realm.copyToRealm(obj)
        realm.commitTransaction()

        return t
    }

    // Singleton ***********************************************************************************
    private object Holder {
        val INSTANCE = DBservice()
    }

    companion object{
        val instance: DBservice by lazy {Holder.INSTANCE}
    }
}