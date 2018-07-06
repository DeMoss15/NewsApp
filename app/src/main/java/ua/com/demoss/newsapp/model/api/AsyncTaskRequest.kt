package ua.com.demoss.newsapp.model.api

import android.os.AsyncTask
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.com.demoss.newsapp.model.api.response.ResponseObject

class AsyncTaskRequest(private val newsApi: NewsApi, private val listener: AsyncTaskRequestListener): AsyncTask<Map<String,String>, Unit, Call<ResponseObject>>() {

    override fun doInBackground(vararg params: Map<String, String>?): Call<ResponseObject> {
        return newsApi.getNews(params[0]!!)
    }

    override fun onPostExecute(result: Call<ResponseObject>) {
        super.onPostExecute(result)

        result.enqueue(object: Callback<ResponseObject> {

            override fun onResponse(call: Call<ResponseObject>?, response: Response<ResponseObject>?) {
                Log.i("Api", response?.toString())
                listener.onPostExecute(response)
            }

            override fun onFailure(call: Call<ResponseObject>?, t: Throwable?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.i("Api", call?.toString())
            }
        })
    }

    interface AsyncTaskRequestListener{
        fun onPostExecute(response: Response<ResponseObject>?)
    }
}