package ua.com.demoss.newsapp.model.repository

interface Repository<T> {

    // CRUD
    fun create(item: T)
    fun get(): List<T>
    //fun update(item: T) // Unused
    fun remove(item: T)
}