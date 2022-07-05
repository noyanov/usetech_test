package com.noyanov.usetech_test

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class BookAdapter     // creating constructor for array list and context.
    (// creating variables for arraylist and context.
    private val bookInfoArrayList: ArrayList<BookInfo>, private val mcontext: Context
) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        // inflating our layout for item of recycler view item.
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.book_rv_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        // inside on bind view holder method we are
        // setting ou data to each UI component.
        val (title, subtitle, authors, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink) = bookInfoArrayList[position]
        holder.nameTV.text = title
        holder.publisherTV.text = publisher
        holder.pageCountTV.text = "No of Pages : $pageCount"
        holder.dateTV.text = publishedDate

        // below line is use to set image from URL in our image view.
        Picasso.get().load(thumbnail).into(holder.bookIV)

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener { // inside on click listener method we are calling a new activity
            // and passing all the data of that item in next intent.
            val i = Intent(mcontext, BookDetailsActivity::class.java)
            i.putExtra("title", title)
            i.putExtra("subtitle", subtitle)
            i.putExtra("authors", authors)
            i.putExtra("publisher", publisher)
            i.putExtra("publishedDate", publishedDate)
            i.putExtra("description", description)
            i.putExtra("pageCount", pageCount)
            i.putExtra("thumbnail", thumbnail)
            i.putExtra("previewLink", previewLink)
            i.putExtra("infoLink", infoLink)
            i.putExtra("buyLink", buyLink)

            // after passing that data we are
            // starting our new intent.
            mcontext.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        // inside get item count method we
        // are returning the size of our array list.
        return bookInfoArrayList.size
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // below line is use to initialize
        // our text view and image views.
        var nameTV: TextView
        var publisherTV: TextView
        var pageCountTV: TextView
        var dateTV: TextView
        var bookIV: ImageView

        init {
            nameTV = itemView.findViewById(R.id.idTVBookTitle)
            publisherTV = itemView.findViewById(R.id.idTVpublisher)
            pageCountTV = itemView.findViewById(R.id.idTVPageCount)
            dateTV = itemView.findViewById(R.id.idTVDate)
            bookIV = itemView.findViewById(R.id.idIVbook)
        }
    }
}

