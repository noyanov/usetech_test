package com.noyanov.usetech_test

import android.app.Application
import com.noyanov.usetech_test.db.BookDatabase
import com.noyanov.usetech_test.db.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UsetechApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { BookDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { BookRepository(database.bookDao()) }
}
