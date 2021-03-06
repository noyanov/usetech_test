package com.noyanov.usetech_test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.noyanov.usetech_test.db.BookInfo
import com.noyanov.usetech_test.db.BookInfoRoom
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject


class FindBookActivity : AppCompatActivity() {
    // creating variables for our request queue,
    // array list, progressbar, edittext,
    // image button and our recycler view.
    private var mRequestQueue: RequestQueue? = null
    private var bookInfoArrayList: ArrayList<BookInfo>? = null
    private var progressBar: ProgressBar? = null
    private var searchEdt: EditText? = null
    private var searchBtn: ImageButton? = null
    private val usetechViewModel: UsetechViewModel by viewModels {
        UsetechViewModelFactory((application as UsetechApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        // initializing our views.
        progressBar = findViewById(R.id.idLoadingPB)
        searchEdt = findViewById(R.id.idEdtSearchBooks)
        searchBtn = findViewById(R.id.idBtnSearch)

        // initializing on click listener for our button.
        searchBtn?.setOnClickListener(View.OnClickListener {
            progressBar?.setVisibility(View.VISIBLE)

            // checking if our edittext field is empty or not.
            if (searchEdt?.getText().toString().isEmpty()) {
                searchEdt?.setError("Please enter search query")
                return@OnClickListener
            }
            // if the search query is not empty then we are
            // calling get book info method to load all
            // the books from the API.
            getBooksInfo(searchEdt?.getText().toString())
        })
    }

    private fun getBooksInfo(query: String) {

        // creating a new array list.
        bookInfoArrayList = ArrayList()

        // below line is use to initialize
        // the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(this@FindBookActivity)

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue?.getCache()?.clear()

        // below is the url for getting data from API in json format.
        val url = "https://www.googleapis.com/books/v1/volumes?q=$query"

        // below line we are creating a new request queue.
        val queue: RequestQueue = Volley.newRequestQueue(this@FindBookActivity)


        // below line is use to make json object request inside that we
        // are passing url, get method and getting json object. .
        val booksObjrequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                progressBar!!.visibility = View.GONE
                // inside on response method we are extracting all our json data.
                try {
                    //val obj: JSONObject = response
                    val itemsArray = response.getJSONArray("items")
                    for (i in 0 until itemsArray.length()) {
                        try {
                            val itemsObj = itemsArray.getJSONObject(i)
                            val bookInfo = BookInfo(itemsObj)
                            bookInfo.isFavorite = false
                            usetechViewModel.setIsFavorite(bookInfo)
                            // below line is use to pass our modal
                            // class in our array list.
                            bookInfoArrayList!!.add(bookInfo)
                        } catch(e: JSONException) {
                            // just skip the item
                        }
                    }
                    // below line is use to pass our
                    // array list in adapter class.
                    val adapter = BookAdapter(bookInfoArrayList!!, this@FindBookActivity)

                    // below line is use to add linear layout
                    // manager for our recycler view.
                    val linearLayoutManager =
                        LinearLayoutManager(this@FindBookActivity, RecyclerView.VERTICAL, false)
                    val mRecyclerView = findViewById<View>(R.id.idRVBooks) as RecyclerView

                    // in below line we are setting layout manager and
                    // adapter to our recycler view.
                    mRecyclerView.layoutManager = linearLayoutManager
                    mRecyclerView.adapter = adapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // displaying a toast message when we get any error from API
                    Toast.makeText(this@FindBookActivity, "No Data Found$e", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this@FindBookActivity, "Error found is $error", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == BookAdapter.detailsBookActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val bookid = intentData?.getStringExtra(BookDetailsActivity.EXTRA_REPLY_ID)
            val json = intentData?.getStringExtra(BookDetailsActivity.EXTRA_REPLY_JSON)
            if(bookid != null && json != null) {
                val book = BookInfoRoom(bookid, json)
                //usetechViewModel.insert(book)
            }
        }
        else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }


}

