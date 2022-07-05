package com.noyanov.usetech_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FavoriteBookListAdapter : ListAdapter<BookInfoRoom, FavoriteBookListAdapter.FavoriteBookViewHolder>(BOOKS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteBookViewHolder {
        return FavoriteBookViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteBookViewHolder, position: Int) {
        val current = getItem(position)
        val bi = current.getBookInfo()
        if(bi != null)
            holder.bind(bi)
    }

    class FavoriteBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var bookItemView: TextView
        var nameTV: TextView
        var publisherTV: TextView
        var pageCountTV: TextView
        var dateTV: TextView
        var bookIV: ImageView

        init {
            //bookItemView = itemView.findViewById(R.id.textView)
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
            Picasso.get().load(bi.thumbnail).into(bookIV)
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
}
