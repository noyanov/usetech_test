package com.noyanov.usetech_test.db

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONException

import org.json.JSONObject




/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "book_table")
class BookInfoRoom(
    @PrimaryKey @ColumnInfo(name = "bookid") val bookid: String,
    @ColumnInfo(name = "json") val json: String) {
        fun getBookInfo(): BookInfo? {
            try {
                val jsonObject = JSONObject(this.json)
                val bi = BookInfo(jsonObject)
                return bi
            } catch (err: JSONException) {
                Log.d("Error", err.toString())
            }
            return null
        }
}
