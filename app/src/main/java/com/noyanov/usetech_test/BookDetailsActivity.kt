package com.noyanov.usetech_test

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.noyanov.usetech_test.db.BookInfoRoom
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class BookDetailsActivity : AppCompatActivity() {

    // creating variables for strings,text view, image views and button.
    var title: String? = null
    var subtitle: String? = null
    var publisher: String? = null
    var publishedDate: String? = null
    var description: String? = null
    var thumbnail: String? = null
    var previewLink: String? = null
    var infoLink: String? = null
    var buyLink: String? = null
    var pageCount = 0
    private val authors: ArrayList<String>? = null
    var bookid : String? = null
    var json : String? = null
    var isFavorite : Boolean = false

    var titleTV: TextView? = null
    var subtitleTV: TextView? = null
    var publisherTV: TextView? = null
    var descTV: TextView? = null
    var pageTV: TextView? = null
    var publishDateTV: TextView? = null
    var previewBtn: Button? = null
    var buyBtn: Button? = null
    var idBtnAddToFavorites : Button? = null
    private var bookIV: ImageView? = null

    private val usetechViewModel: UsetechViewModel by viewModels {
        UsetechViewModelFactory((application as UsetechApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        // initializing our views..
        titleTV = findViewById(R.id.idTVTitle)
        subtitleTV = findViewById(R.id.idTVSubTitle)
        publisherTV = findViewById(R.id.idTVpublisher)
        descTV = findViewById(R.id.idTVDescription)
        pageTV = findViewById(R.id.idTVNoOfPages)
        publishDateTV = findViewById(R.id.idTVPublishDate)
        previewBtn = findViewById(R.id.idBtnPreview)
        buyBtn = findViewById(R.id.idBtnBuy)
        idBtnAddToFavorites = findViewById(R.id.idBtnAddToFavorites)
        bookIV = findViewById(R.id.idIVbook)

        // getting the data which we have passed from our adapter class.
        title = intent.getStringExtra("title")
        subtitle = intent.getStringExtra("subtitle")
        publisher = intent.getStringExtra("publisher")
        publishedDate = intent.getStringExtra("publishedDate")
        description = intent.getStringExtra("description")
        pageCount = intent.getIntExtra("pageCount", 0)
        thumbnail = intent.getStringExtra("thumbnail")
        previewLink = intent.getStringExtra("previewLink")
        infoLink = intent.getStringExtra("infoLink")
        buyLink = intent.getStringExtra("buyLink")
        bookid = intent.getStringExtra("bookid")
        json = intent.getStringExtra("json")
        isFavorite = intent.getBooleanExtra("isFavorite", false)

        // after getting the data we are setting
        // that data to our text views and image view.
        titleTV?.setText(title)
        subtitleTV?.setText(subtitle)
        publisherTV?.setText(publisher)
        publishDateTV?.setText("Published On : $publishedDate")
        descTV?.setText(description)
        pageTV?.setText("No Of Pages : $pageCount")
        Picasso.get().load(thumbnail).into(bookIV)

        // adding on click listener for our preview button.
        previewBtn?.setOnClickListener(View.OnClickListener {
            if (previewLink != null && previewLink!!.isEmpty()) {
                // below toast message is displayed when preview link is not present.
                Toast.makeText(this@BookDetailsActivity, "No preview Link present", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            // if the link is present we are opening
            // that link via an intent.
            try {
                val uri = Uri.parse(previewLink)
                val i = Intent(Intent.ACTION_VIEW, uri)
                startActivity(i)
            } catch(e:Exception) {
                Log.e("BUG", e.localizedMessage)
            }
        })

        // initializing on click listener for buy button.
        buyBtn?.setOnClickListener(View.OnClickListener {
            if (buyLink != null && buyLink!!.isEmpty()) {
                // below toast message is displaying when buy link is empty.
                Toast.makeText(
                    this@BookDetailsActivity,
                    "No buy page present for this book",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            // if the link is present we are opening
            // the link via an intent.
            val uri = Uri.parse(buyLink)
            val i = Intent(Intent.ACTION_VIEW, uri)
            startActivity(i)
        })

        if(bookid != null) {
            val abookid = bookid ?: ""
            //CoroutineScope.launch { checkIsBookFavorite() }
            //runBlocking {
            //    val isFavorite: Boolean = usetechViewModel.isFavoriteBook(abookid).await()
                if(!isFavorite) { // the book is not in favorites -> we can add it
                    idBtnAddToFavorites?.setText(R.string.add_favorites_title)
                    idBtnAddToFavorites?.setOnClickListener {
                        if (bookid != null && json != null) {
                            val book = BookInfoRoom(bookid!!, json!!)
                            usetechViewModel.insert(book)
                        }
                        val replyIntent = Intent()
                        replyIntent.putExtra(BookDetailsActivity.EXTRA_REPLY_ID, bookid)
                        replyIntent.putExtra(BookDetailsActivity.EXTRA_REPLY_JSON, json)
                        setResult(Activity.RESULT_OK, replyIntent)
                        finish()
                    }
                } else { // else the book is already in favorites -> we can remove it from there
                    idBtnAddToFavorites?.setText(R.string.remove_favorites_title)
                    idBtnAddToFavorites?.setOnClickListener {
                        if (bookid != null) {
                            usetechViewModel.delete(bookid!!)
                        }
                        val replyIntent = Intent()
                        //                    replyIntent.putExtra(BookDetailsActivity.EXTRA_REPLY_ID, bookid)
                        //                    replyIntent.putExtra(BookDetailsActivity.EXTRA_REPLY_JSON, json)
                        setResult(Activity.RESULT_OK, replyIntent)
                        finish()
                    }
                }
            //}

        } else {
            idBtnAddToFavorites?.visibility = View.GONE
        }



    }

    companion object {
        const val EXTRA_REPLY_ID = "com.usetech.REPLY_ID"
        const val EXTRA_REPLY_JSON = "com.usetech.REPLY_JSON"
    }
}

