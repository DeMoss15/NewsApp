package ua.com.demoss.newsapp

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import ua.com.demoss.newsapp.di.component.AppComponent
import ua.com.demoss.newsapp.di.component.DaggerAppComponent
import ua.com.demoss.newsapp.di.module.AppModule
import ua.com.demoss.newsapp.model.db.RealmMigrations

class NewsApp: Application() {

    companion object{
        @JvmStatic lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initDagger()
        Realm.init(this)
        val configuration = RealmConfiguration
                .Builder()
                .name("newsappdb.realm")
                .schemaVersion(1)
                .migration(RealmMigrations())
                .build()
        Realm.setDefaultConfiguration(configuration)
    }

    override fun onTerminate() {
        super.onTerminate()

        Realm.getDefaultInstance().close()
    }

    private fun initDagger(){
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}