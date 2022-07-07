package com.noyanov.usetech_test

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noyanov.usetech_test.db.BookInfo
import com.squareup.picasso.Picasso


class BookAdapter     // creating constructor for array list and context.
    (// creating variables for arraylist and context.
    private var bookInfoArrayList: ArrayList<BookInfo>, private val mcontext: Context
    ) : RecyclerView.Adapter<BookAdapter.BookViewHolder>(), Filterable {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            // inflating our layout for item of recycler view item.
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.book_rv_item, parent, false)
            return BookViewHolder(view)
    }

    companion object {
        val detailsBookActivityRequestCode = 3
    }


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        // inside on bind view holder method we are
        // setting ou data to each UI component.
//        val (title, subtitle, authors, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink, bookid, json) = bookInfoArrayList[position]
        val bi = bookInfoArrayList[position]
        holder.nameTV.text = bi.title
        holder.publisherTV.text = bi.publisher
        holder.pageCountTV.text = "No of Pages : ${bi.pageCount}"
        holder.dateTV.text = bi.publishedDate
        holder.favoriteIV.visibility = if(bi.isFavorite) View.VISIBLE else View.GONE

        // below line is use to set image from URL in our image view.
        Picasso.get().load(bi.thumbnail).into(holder.bookIV)

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener { // inside on click listener method we are calling a new activity
            // and passing all the data of that item in next intent.
            val i = Intent(mcontext, BookDetailsActivity::class.java)
            i.putExtra("title", bi.title)
            i.putExtra("subtitle", bi.subtitle)
            i.putExtra("authors", bi.authors)
            i.putExtra("publisher", bi.publisher)
            i.putExtra("publishedDate", bi.publishedDate)
            i.putExtra("description", bi.description)
            i.putExtra("pageCount", bi.pageCount)
            i.putExtra("thumbnail", bi.thumbnail)
            i.putExtra("previewLink", bi.previewLink)
            i.putExtra("infoLink", bi.infoLink)
            i.putExtra("buyLink", bi.buyLink)
            i.putExtra("json", bi.json)
            i.putExtra("bookid", bi.bookid)
            i.putExtra("isFavorite", bi.isFavorite)

            // after passing that data we are
            // starting our new intent.
            mcontext.startActivity(i) //, detailsBookActivityRequestCode)
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
        var favoriteIV: ImageView

        init {
            nameTV = itemView.findViewById(R.id.idTVBookTitle)
            publisherTV = itemView.findViewById(R.id.idTVpublisher)
            pageCountTV = itemView.findViewById(R.id.idTVPageCount)
            dateTV = itemView.findViewById(R.id.idTVDate)
            bookIV = itemView.findViewById(R.id.idIVbook)
            favoriteIV = itemView.findViewById(R.id.idIVFavorites)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                var bookInfoArrayListFiltered : ArrayList<BookInfo> = ArrayList()
                if (charString.isEmpty()) bookInfoArrayListFiltered = bookInfoArrayList else {
                    val filteredList = ArrayList<BookInfo>()
                    bookInfoArrayList
                        .filter {
                            (it.title.contains(constraint!!)) or
                            (it.subtitle.contains(constraint) or
                            (it.authors.joinToString().contains(constraint)) )
                        }
                        .forEach { filteredList.add(it) }
                    bookInfoArrayListFiltered = filteredList

                }
                return FilterResults().apply { values = bookInfoArrayListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                bookInfoArrayList = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<BookInfo>
                notifyDataSetChanged()
            }
        }
    }
}

