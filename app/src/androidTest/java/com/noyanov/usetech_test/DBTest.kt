package com.noyanov.usetech_test

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noyanov.usetech_test.db.BookDao
import com.noyanov.usetech_test.db.BookDatabase
import com.noyanov.usetech_test.db.BookInfoRoom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DBTest {
    private lateinit var dao: BookDao
    private lateinit var db: BookDatabase

    private val test_bookid = "G7meAAAAMAAJ"
    private val test_json =
        "{\"kind\":\"books#volume\",\"id\":\"G7meAAAAMAAJ\",\"etag\":\"RILuEM3u3Z8\",\"selfLink\":\"https:\\/\\/www.googleapis.com\\/books\\/v1\\/volumes\\/G7meAAAAMAAJ\",\"volumeInfo\":{\"title\":\"Test Accommodations for Students with Disabilities\",\"authors\":[\"Edward Burns\"],\"publisher\":\"Charles C Thomas Pub Limited\",\"publishedDate\":\"1998\",\"industryIdentifiers\":[{\"type\":\"OTHER\",\"identifier\":\"UOM:39015043814501\"}],\"readingModes\":{\"text\":false,\"image\":false},\"pageCount\":332,\"printType\":\"BOOK\",\"categories\":[\"Education\"],\"maturityRating\":\"NOT_MATURE\",\"allowAnonLogging\":false,\"contentVersion\":\"2.3.2.0.preview.0\",\"panelizationSummary\":{\"containsEpubBubbles\":false,\"containsImageBubbles\":false},\"imageLinks\":{\"smallThumbnail\":\"http:\\/\\/books.google.com\\/books\\/content?id=G7meAAAAMAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api\",\"thumbnail\":\"http:\\/\\/books.google.com\\/books\\/content?id=G7meAAAAMAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api\"},\"language\":\"en\",\"previewLink\":\"http:\\/\\/books.google.ru\\/books?id=G7meAAAAMAAJ&q=Test&dq=Test&hl=&cd=2&source=gbs_api\",\"infoLink\":\"http:\\/\\/books.google.ru\\/books?id=G7meAAAAMAAJ&dq=Test&hl=&source=gbs_api\",\"canonicalVolumeLink\":\"https:\\/\\/books.google.com\\/books\\/about\\/Test_Accommodations_for_Students_with_Di.html?hl=&id=G7meAAAAMAAJ\"},\"saleInfo\":{\"country\":\"RU\",\"saleability\":\"NOT_FOR_SALE\",\"isEbook\":false},\"accessInfo\":{\"country\":\"RU\",\"viewability\":\"NO_PAGES\",\"embeddable\":false,\"publicDomain\":false,\"textToSpeechPermission\":\"ALLOWED\",\"epub\":{\"isAvailable\":false},\"pdf\":{\"isAvailable\":false},\"webReaderLink\":\"http:\\/\\/play.google.com\\/books\\/reader?id=G7meAAAAMAAJ&hl=&printsec=frontcover&source=gbs_api\",\"accessViewStatus\":\"NONE\",\"quoteSharingAllowed\":false},\"searchInfo\":{\"textSnippet\":\"Practice <b>Tests<\\/b> Practice <b>tests<\\/b> provide an opportunity to acquaint the student with the task, to determine if the student requires other accommodations, and to otherwise determine whether the suitability of the instructions, the <b>test<\\/b>,&nbsp;...\"}}"

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BookDatabase::class.java).build()
        dao = db.bookDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun addBookAndCheckCount() {
        GlobalScope.launch {
            val book = BookInfoRoom(test_bookid, test_json)
            dao.insert(book)
            val count = dao.getCount()
            assertThat(count, equalTo(1))
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeBookAndReadInList() {
        GlobalScope.launch {
            val book = BookInfoRoom(test_bookid, test_json)
            dao.insert(book)
            val firstBook = dao.getAlphabetizedBooks().take(1).first()
            assertThat(firstBook, equalTo(book))
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeBookAndReadById() {

        GlobalScope.async {
            val book = BookInfoRoom(test_bookid, test_json)
            dao.insert(book)
            val byId = dao.getBookById(book.bookid)
            assertThat(byId, equalTo(book))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllForEmpty() {

        GlobalScope.async {
            dao.deleteAll()
            val count = dao.getCount()
            assertThat(count, equalTo(0))
        }
    }

    @Test
    @Throws(Exception::class)
    fun addAndDeleteAndCheckCount() {

        GlobalScope.async {
            val book = BookInfoRoom(test_bookid, test_json)
            dao.insert(book)
            dao.delete(test_bookid)
            val count = dao.getCount()
            assertThat(count, equalTo(0))
        }
    }

    @Test
    @Throws(Exception::class)
    fun addAndDeleteAndAddAgainCheckCount() {

        GlobalScope.async {
            val book = BookInfoRoom(test_bookid, test_json)
            dao.insert(book)
            dao.delete(test_bookid)
            val book2 = BookInfoRoom(test_bookid, test_json)
            dao.insert(book2)
            val count = dao.getCount()
            assertThat(count, equalTo(1))
        }
    }

}