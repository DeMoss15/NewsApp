package ua.com.demoss.newsapp.model.api.request

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class QueryMapBuilder {

    companion object {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        const val SORT_BY_RELEVANCY = "relevancy"
        const val SORT_BY_POPULARITY = "popularity"
        const val PAGE_SIZE: Int = 20 //There is no literal for Int, only for Long
    }

    private val queryMap = HashMap<String, String>()

    init {
        queryMap["apiKey"] = "3b199916184a4ef29471a98ef08e8e16"
        queryMap["pageSize"] = PAGE_SIZE.toString()
        queryMap["page"] = "1"
        queryMap["q"] = "apple"
    }

    // Query ***************************************************************************************
    fun query(text: String){
        queryMap.remove("q")
        queryMap["q"] = text
    }

    // Pagination **********************************************************************************
    fun page(page: Int){
        queryMap["page"] = page.toString()
    }

    // Sorting *************************************************************************************
    fun sortBy(sortBy: String){
        queryMap["sortBy"] = sortBy
        queryMap.put("sortBy", sortBy)
    }

    // Filtering ***********************************************************************************
    // date ================
    fun from(date: String){
        queryMap["from"] = date
    }

    fun from(date: Calendar){
        queryMap["from"] = simpleDateFormat.format(date.time)
    }

    fun to(date: String){
        queryMap["to"] = date
    }

    fun to(date: Calendar){
        queryMap["to"] = simpleDateFormat.format(date.time)
    }

    // Builder *************************************************************************************
    fun build(): Map<String, String>{
        Log.i("Api", queryMap.toString())
        return queryMap
    }
}