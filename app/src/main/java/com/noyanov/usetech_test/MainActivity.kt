package com.noyanov.usetech_test


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Filterable
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noyanov.usetech_test.db.BookInfoRoom

class MainActivity : AppCompatActivity() {

//    private val newWordActivityRequestCode = 1
    private val addBookActivityRequestCode = 2
    private val usetechViewModel: UsetechViewModel by viewModels {
        UsetechViewModelFactory((application as UsetechApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = FavoriteBookListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchView = findViewById<SearchView>(R.id.idEdtFilterBooks)
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
//            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
//            startActivityForResult(intent, newWordActivityRequestCode)
            val intent = Intent(this@MainActivity, FindBookActivity::class.java)
            startActivityForResult(intent, addBookActivityRequestCode)
        }

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        usetechViewModel.allBooks.observe(owner = this) { books ->
            // Update the cached copy of the words in the adapter.
            books.let { adapter.submitList(it) }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        if (requestCode == addBookActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
            recyclerView.adapter?.notifyDataSetChanged()
//            intentData?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let { reply ->
//                val book = BookInfoRoom(reply, reply)
//                usetechViewModel.insert(book)
//            }
        }
//        else {
//            Toast.makeText(
//                applicationContext,
//                R.string.empty_not_saved,
//                Toast.LENGTH_LONG
//            ).show()
//        }
    }
}
