package com.noyanov.usetech_test

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class UsetechViewModel(private val repository: BookRepository) : ViewModel() {

        // Using LiveData and caching what allWords returns has several benefits:
        // - We can put an observer on the data (instead of polling for changes) and only update the
        //   the UI when the data actually changes.
        // - Repository is completely separated from the UI through the ViewModel.
        val allBooks: LiveData<List<BookInfoRoom>> = repository.allBooks.asLiveData()

        /**
         * Launching a new coroutine to insert the data in a non-blocking way
         */
        fun insert(book: BookInfoRoom) = viewModelScope.launch {
                repository.insert(book)
        }
}

class UsetechViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UsetechViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return UsetechViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
        }
}
