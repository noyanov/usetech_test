package com.noyanov.usetech_test.db


import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * The Room Magic is in this file, where you map a method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface BookDao {

    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM book_table ORDER BY bookid ASC")
    fun getAlphabetizedBooks(): Flow<List<BookInfoRoom>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: BookInfoRoom)

    @Query("DELETE FROM book_table WHERE bookid = :bookId")
    suspend fun delete(bookId: String): Int

    @Query("DELETE FROM book_table")
    suspend fun deleteAll()

    @Query("SELECT COUNT() FROM book_table")
    suspend fun getCount() : Int

    @Query("SELECT * FROM book_table WHERE bookid = :bookId")
    suspend fun getBookById(bookId: String?): BookInfoRoom?
}
