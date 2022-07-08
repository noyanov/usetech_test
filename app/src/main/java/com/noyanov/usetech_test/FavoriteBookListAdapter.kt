package com.noyanov.usetech_test

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noyanov.usetech_test.db.BookInfo
import com.noyanov.usetech_test.db.BookInfoRoom
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.util.*
import kotlin.collections.ArrayList


class FavoriteBookListAdapter(private val mcontext: Context) : ListAdapter<BookInfoRoom, FavoriteBookListAdapter.FavoriteBookViewHolder>(BOOKS_COMPARATOR), Filterable
{
    var initialList : List<BookInfoRoom> = ArrayList()

//    fun setData(list: List<BookInfoRoom>) {
//        this.initialList = list
//        submitList(list)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteBookViewHolder {
        return FavoriteBookViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteBookViewHolder, position: Int) {
        val current = getItem(position)
        val bi = current.getBookInfo()
        if(bi != null) {
            holder.bind(bi)

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
    }

    class FavoriteBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        fun bind(bi: BookInfo) {
            //bookItemView.text = text
            nameTV.text = bi.title
            publisherTV.text = bi.publisher
            pageCountTV.text = "No of Pages : ${bi.pageCount}"
            dateTV.text = bi.publishedDate

            // below line is use to set image from URL in our image view.
//            Picasso.get().load(bi.thumbnail).memoryPolicy(MemoryPolicy.NO_CACHE).into(bookIV, object : Callback {
//                override fun onSuccess() {
//                    bookIV.setVisibility(View.GONE)
//                }
//                override fun onError(e : Exception) {
//                    bookIV.setVisibility(View.VISIBLE)
//                }
//            })

            Picasso.get().load(bi.thumbnail).into(bookIV);
        }

        companion object {
            fun create(parent: ViewGroup): FavoriteBookViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.book_rv_item, parent, false)
                return FavoriteBookViewHolder(view)
            }
        }
    }

    companion object {
        private val BOOKS_COMPARATOR = object : DiffUtil.ItemCallback<BookInfoRoom>() {
            override fun areItemsTheSame(oldItem: BookInfoRoom, newItem: BookInfoRoom): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: BookInfoRoom, newItem: BookInfoRoom): Boolean {
                return oldItem.bookid == newItem.bookid
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {val charString = constraint?.toString() ?: ""
                if(initialList.size == 0)
                    initialList = currentList
                var bookInfoArrayListFiltered : ArrayList<BookInfoRoom> = ArrayList()
                if (charString.isEmpty()) bookInfoArrayListFiltered = ArrayList(initialList) else {
                    val filteredList = ArrayList<BookInfoRoom>()
                    initialList
                        .forEach {
                            val ti = it.getBookInfo();
                            if(ti != null) {
                                if (ti.title.contains(constraint!!) or
                                    ti.subtitle.contains(constraint) or
                                    ti.authors.joinToString().contains(constraint)
                                ) {
                                    filteredList.add(it)
                                }
                            }
                        }
                    bookInfoArrayListFiltered = filteredList
                }
                return FilterResults().apply { values = bookInfoArrayListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filtered : MutableList<BookInfoRoom> = (results?.values) as MutableList<BookInfoRoom>
                submitList(filtered)
//                bookInfoArrayListFiltered = if (results?.values == null)
//                    ArrayList()
//                else
//                    results.values as ArrayList<BookInfoRoom>
//                notifyDataSetChanged()
            }
        }
    }
}
