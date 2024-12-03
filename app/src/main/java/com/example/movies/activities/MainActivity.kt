package com.example.movies.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.adapter.MoviesAdapter
import com.example.movies.data.entities.Movie
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.utils.RetrofitProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MoviesAdapter
    private var movieList: MutableList<Movie> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adapter = MoviesAdapter(movieList) { position ->
            val movie = movieList[position]
            navigateToDetail(movie)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1) //Grid layout with one column.
        fetchMovies("marvel")
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.title)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)

        val menuItem = menu?.findItem(R.id.menu_search)!!
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                fetchMovies(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    //Fetches movies from the server
    private fun fetchMovies(query: String) {
        // Show loading progress bar
        binding.loadingProgressBar.visibility = View.VISIBLE

        // Create Retrofit instance
        val apiService = RetrofitProvider.getRetrofit()

        // Launch a coroutine for background task
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Make the API call and get the response
                val response = apiService.fetchMoviesByTitle(query, "fb7aca4")

                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    // Hide the loading progress bar
                    binding.loadingProgressBar.visibility = View.GONE

                    // If movies are found, update the RecyclerView and adapter
                    if (response.response == "True") {
                        binding.emptyView.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE

                        // Clear old movie list and add new items from the response
                        movieList.clear()
                        movieList.addAll(response.search) // Ensure response.search is a List<Movie>
                        adapter.updateItems(movieList)
                    } else {
                        // Show empty view if no results
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                        binding.noResultsTextView.text = getString(R.string.no_results, query)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("APIError", "Error fetching movies: ${e.message}")
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                    binding.noResultsTextView.text = getString(R.string.no_results)
                }
            }
        }
    }
}