package ua.com.demoss.newsapp.ui

import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.activity_main.*
import ua.com.demoss.newsapp.R
import ua.com.demoss.newsapp.presenter.NewsPresenter
import ua.com.demoss.newsapp.ui.adapter.ApiArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.ui.adapter.RealmArticlesRecyclerViewAdapter
import ua.com.demoss.newsapp.view.NewsView
import java.text.SimpleDateFormat
import java.util.*
import android.view.View
import android.widget.*
import ua.com.demoss.newsapp.model.api.request.QueryMapBuilder
import android.support.customtabs.CustomTabsIntent




class MainActivity : MvpAppCompatActivity(), NewsView, DatePickerDialog.OnDateSetListener {

    /*
    * Sorry, I'm too lazy to create UI for source selection,
    * but I've made a method for it in QueryMapBuilder and NewsPresenter
    */

    // Variables ***********************************************************************************
    @InjectPresenter
    lateinit var presenter: NewsPresenter

    var firstDate: GregorianCalendar? = null

    // Activity ************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_button_favorites.setOnClickListener{presenter.switchToFavorites()}
        activity_main_button_api.setOnClickListener { presenter.switchToApi() }

        activity_main_recycler_view_articles.layoutManager = LinearLayoutManager(this)
        activity_main_recycler_view_articles.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy > 0){// Recycle view scrolling downwards...

                            if (!recyclerView?.canScrollVertically(RecyclerView.FOCUS_DOWN)!!){// can't scroll more
                                presenter.getNextPage()
                            }
                        }
                    }
                }
        )

        activity_main_search_view_query.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.setQuery(query?.toLowerCase() ?: "apple")
                return true
            }
        })

        activity_main_text_view_from_date.setOnClickListener {
            val today = GregorianCalendar()
            val datePickerDialog = DatePickerDialog(
                    this,
                    this,
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
            firstDate = null
        }

        activity_main_text_view_to_date.setOnClickListener {
            val today = GregorianCalendar()
            val datePickerDialog = DatePickerDialog(
                    this,
                    this,
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        // Spinner
        val spinnerList = arrayOf(QueryMapBuilder.SORT_BY_POPULARITY, QueryMapBuilder.SORT_BY_RELEVANCY)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activity_main_spinner_sort_by.adapter = adapter
        activity_main_spinner_sort_by.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.sortBy(spinnerList[position])
            }
        }

        presenter.onCreate(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    // NewsView ************************************************************************************
    override fun setAdapter(adapter: RealmArticlesRecyclerViewAdapter) {
        activity_main_recycler_view_articles.adapter = adapter
    }

    override fun setAdapter(adapter: ApiArticlesRecyclerViewAdapter) {
        activity_main_recycler_view_articles.adapter = adapter
    }

    override fun shareDialog(content: ShareLinkContent) {
        ShareDialog.show(this, content)
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun openUri(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    // OnDateSetListener ***************************************************************************
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val date = GregorianCalendar(year, month, day)

        if (firstDate == null) {
            activity_main_text_view_from_date.text = StringBuilder("From: ${sdf.format(date.time)}").toString()
            presenter.setFrom(date)
            firstDate = date
        } else {
            if (date < firstDate){
                showToast("You cannot set dates in reverse order")
                return
            }
            activity_main_text_view_to_date.text = StringBuilder("To: ${sdf.format(date.time)}").toString()
            presenter.setTo(date)
        }
    }
}
