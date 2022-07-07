package com.noyanov.usetech_test.db

import org.json.JSONException
import org.json.JSONObject


class BookInfo     // creating a constructor class for our BookInfo
{
    // creating getter and setter methods
    // creating string, int and array list
    // variables for our book details
    val title: String
    val subtitle: String
    val authors: ArrayList<String>
    val publisher: String
    val publishedDate: String
    val description: String
    val pageCount: Int
    val thumbnail: String
    val previewLink: String
    val infoLink: String
    val buyLink: String

    val bookid : String
    val json : String

    var isFavorite : Boolean

    //    constructor(
//        // creating getter and setter methods
//        // creating string, int and array list
//        // variables for our book details
//        bookid : String,
//        json : String,
//        title: String, subtitle: String, authors: ArrayList<String>, publisher: String,
//        publishedDate: String, description: String, pageCount: Int, thumbnail: String,
//        previewLink: String, infoLink: String, buyLink: String) {}

    constructor(itemsObj: JSONObject)
    {
        this.json = itemsObj.toString()
        this.bookid = itemsObj.optString("id")
        val volumeObj = itemsObj.getJSONObject("volumeInfo")
        this.title = volumeObj.optString("title")
        this.subtitle = volumeObj.optString("subtitle")
        val authorsArrayList = ArrayList<String>()
        try {
            val authorsArray = volumeObj.getJSONArray("authors")
            if (authorsArray.length() != 0) {
                for (j in 0 until authorsArray.length()) {
                    authorsArrayList.add(authorsArray.optString(j))
                }
            }
        } catch(e:JSONException) { // just no authors
        }
        this.authors = authorsArrayList
        this.publisher = volumeObj.optString("publisher")
        this.publishedDate = volumeObj.optString("publishedDate")
        this.description = volumeObj.optString("description")
        this.pageCount = volumeObj.optInt("pageCount")
        val imageLinks = volumeObj.optJSONObject("imageLinks")
        this.thumbnail = if(imageLinks != null) imageLinks.optString("thumbnail") else ""
        this.previewLink = volumeObj.optString("previewLink")
        this.infoLink = volumeObj.optString("infoLink")
        val saleInfoObj = itemsObj.optJSONObject("saleInfo")
        this.buyLink = if(saleInfoObj != null) saleInfoObj.optString("buyLink") else ""
        this.isFavorite = true
    }

}

