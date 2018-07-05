package ua.com.demoss.newsapp.model.db

import io.realm.DynamicRealm
import io.realm.RealmMigration

class RealmMigrations: RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema

        val articleSchema = schema.get("RealmArticle")
        articleSchema!!.setNullable("author", true)
        articleSchema.setNullable("title", true)
        articleSchema.setNullable("description", true)
        articleSchema.setNullable("url", true)
        articleSchema.setNullable("urlToImage", true)
        articleSchema.setNullable("publishedAt", true)

        val sourceSchema = schema.get("RealmSource")
        sourceSchema!!.setNullable("name", true)
    }
}