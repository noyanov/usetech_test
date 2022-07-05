package com.noyanov.usetech_test

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    // creating variables for our request queue,
    // array list, progressbar, edittext,
    // image button and our recycler view.
    private var mRequestQueue: RequestQueue? = null
    private var bookInfoArrayList: ArrayList<BookInfo>? = null
    private var progressBar: ProgressBar? = null
    private var searchEdt: EditText? = null
    private var searchBtn: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        mRequestQueue = Volley.newRequestQueue(this@MainActivity)

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue?.getCache()?.clear()

        // below is the url for getting data from API in json format.
        val url = "https://www.googleapis.com/books/v1/volumes?q=$query"

        // below line we are creating a new request queue.
        val queue: RequestQueue = Volley.newRequestQueue(this@MainActivity)


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
                        val itemsObj = itemsArray.getJSONObject(i)
                        val volumeObj = itemsObj.getJSONObject("volumeInfo")
                        val title = volumeObj.optString("title")
                        val subtitle = volumeObj.optString("subtitle")
                        val authorsArray = volumeObj.getJSONArray("authors")
                        val publisher = volumeObj.optString("publisher")
                        val publishedDate = volumeObj.optString("publishedDate")
                        val description = volumeObj.optString("description")
                        val pageCount = volumeObj.optInt("pageCount")
                        val imageLinks = volumeObj.optJSONObject("imageLinks")
                        val thumbnail = if(imageLinks != null) imageLinks.optString("thumbnail") else ""
                        val previewLink = volumeObj.optString("previewLink")
                        val infoLink = volumeObj.optString("infoLink")
                        val saleInfoObj = itemsObj.optJSONObject("saleInfo")
                        val buyLink = if(saleInfoObj != null) saleInfoObj.optString("buyLink") else ""
                        val authorsArrayList = ArrayList<String>()
                        if (authorsArray.length() != 0) {
                            for (j in 0 until authorsArray.length()) {
                                authorsArrayList.add(authorsArray.optString(i))
                            }
                        }
                        // after extracting all the data we are
                        // saving this data in our modal class.
                        val bookInfo = BookInfo(
                            title,
                            subtitle,
                            authorsArrayList,
                            publisher,
                            publishedDate,
                            description,
                            pageCount,
                            thumbnail,
                            previewLink,
                            infoLink,
                            buyLink
                        )

                        // below line is use to pass our modal
                        // class in our array list.
                        bookInfoArrayList!!.add(bookInfo)

                        // below line is use to pass our
                        // array list in adapter class.
                        val adapter = BookAdapter(bookInfoArrayList!!, this@MainActivity)

                        // below line is use to add linear layout
                        // manager for our recycler view.
                        val linearLayoutManager =
                            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
                        val mRecyclerView = findViewById<View>(R.id.idRVBooks) as RecyclerView

                        // in below line we are setting layout manager and
                        // adapter to our recycler view.
                        mRecyclerView.layoutManager = linearLayoutManager
                        mRecyclerView.adapter = adapter
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // displaying a toast message when we get any error from API
                    Toast.makeText(this@MainActivity, "No Data Found$e", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this@MainActivity, "Error found is $error", Toast.LENGTH_SHORT)
                    .show()
            }
        )


//        val booksObjrequest =
//                JsonObjectRequest(Request.Method.GET, url, null, object : Response.Listener<JSONObject?> {
//                fun onResponse(response: JSONObject) {
//                    progressBar!!.visibility = View.GONE
//                    // inside on response method we are extracting all our json data.
//                    try {
//                        val obj: JSONObject = response
//                        val itemsArray = obj.getJSONArray("items")
//                        for (i in 0 until itemsArray.length()) {
//                            val itemsObj = itemsArray.getJSONObject(i)
//                            val volumeObj = itemsObj.getJSONObject("volumeInfo")
//                            val title = volumeObj.optString("title")
//                            val subtitle = volumeObj.optString("subtitle")
//                            val authorsArray = volumeObj.getJSONArray("authors")
//                            val publisher = volumeObj.optString("publisher")
//                            val publishedDate = volumeObj.optString("publishedDate")
//                            val description = volumeObj.optString("description")
//                            val pageCount = volumeObj.optInt("pageCount")
//                            val imageLinks = volumeObj.optJSONObject("imageLinks")
//                            val thumbnail = imageLinks.optString("thumbnail")
//                            val previewLink = volumeObj.optString("previewLink")
//                            val infoLink = volumeObj.optString("infoLink")
//                            val saleInfoObj = itemsObj.optJSONObject("saleInfo")
//                            val buyLink = saleInfoObj.optString("buyLink")
//                            val authorsArrayList = ArrayList<String>()
//                            if (authorsArray.length() != 0) {
//                                for (j in 0 until authorsArray.length()) {
//                                    authorsArrayList.add(authorsArray.optString(i))
//                                }
//                            }
//                            // after extracting all the data we are
//                            // saving this data in our modal class.
//                            val bookInfo = BookInfo(
//                                title,
//                                subtitle,
//                                authorsArrayList,
//                                publisher,
//                                publishedDate,
//                                description,
//                                pageCount,
//                                thumbnail,
//                                previewLink,
//                                infoLink,
//                                buyLink
//                            )
//
//                            // below line is use to pass our modal
//                            // class in our array list.
//                            bookInfoArrayList!!.add(bookInfo)
//
//                            // below line is use to pass our
//                            // array list in adapter class.
//                            val adapter = BookAdapter(bookInfoArrayList!!, this@MainActivity)
//
//                            // below line is use to add linear layout
//                            // manager for our recycler view.
//                            val linearLayoutManager =
//                                LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
//                            val mRecyclerView = findViewById<View>(R.id.idRVBooks) as RecyclerView
//
//                            // in below line we are setting layout manager and
//                            // adapter to our recycler view.
//                            mRecyclerView.layoutManager = linearLayoutManager
//                            mRecyclerView.adapter = adapter
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                        // displaying a toast message when we get any error from API
//                        Toast.makeText(this@MainActivity, "No Data Found$e", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }, object : Response.ErrorListener() {
//                fun onErrorResponse(error: VolleyError) {
//                    // also displaying error message in toast.
//                    Toast.makeText(this@MainActivity, "Error found is $error", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest)
    }
}

