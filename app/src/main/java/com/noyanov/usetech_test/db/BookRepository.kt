package com.noyanov.usetech_test.db

import androidx.annotation.WorkerThread
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class BookRepository(private val bookDao: BookDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allBooks: Flow<List<BookInfoRoom>> = bookDao.getAlphabetizedBooks()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(book: BookInfoRoom) {
        bookDao.insert(book)
    }

    @WorkerThread
    suspend fun delete(bookid: String) {
        bookDao.delete(bookid)
    }

     suspend fun getBookById(bookId: String): BookInfoRoom? {
        return bookDao.getBookById(bookId)
    }

}
