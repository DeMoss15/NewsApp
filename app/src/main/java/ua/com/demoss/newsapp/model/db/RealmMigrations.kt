package ua.com.demoss.newsapp.model.db

import io.realm.DynamicRealm
import io.realm.RealmMigration

class RealmMigrations: RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
    }
}