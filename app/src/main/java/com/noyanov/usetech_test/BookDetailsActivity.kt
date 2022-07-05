package com.noyanov.usetech_test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso


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
    var titleTV: TextView? = null
    var subtitleTV: TextView? = null
    var publisherTV: TextView? = null
    var descTV: TextView? = null
    var pageTV: TextView? = null
    var publishDateTV: TextView? = null
    var previewBtn: Button? = null
    var buyBtn: Button? = null
    private var bookIV: ImageView? = null
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
            if (previewLink!!.isEmpty()) {
                // below toast message is displayed when preview link is not present.
                Toast.makeText(this@BookDetailsActivity, "No preview Link present", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            // if the link is present we are opening
            // that link via an intent.
            val uri = Uri.parse(previewLink)
            val i = Intent(Intent.ACTION_VIEW, uri)
            startActivity(i)
        })

        // initializing on click listener for buy button.
        buyBtn?.setOnClickListener(View.OnClickListener {
            if (buyLink!!.isEmpty()) {
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
    }
}

