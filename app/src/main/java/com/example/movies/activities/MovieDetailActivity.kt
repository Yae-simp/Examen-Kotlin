package com.example.movies.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movies.R
import com.example.movies.data.entities.Movie
import com.example.movies.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"
    }

    private lateinit var binding: MovieDetailActivityBinding
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieDetailActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get movie ID from intent and fetch movie details
        val id = intent.getStringExtra(EXTRA_MOVIE_ID)
        if (id != null) {
            fetchMovie(id)
        } else {
            Log.e("MovieDetailActivity", "Movie ID not found in the intent")
            finish() // Close the activity if no movie ID is provided
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()  // Close the activity on back button press
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {
        supportActionBar?.title = movie.title
        Picasso.get().load(movie.poster).into(binding.posterImageView)

        binding.itemMovieTitle.text = movie.title
        binding.countryTextView.text = movie.country
        binding.runtimeTextView.text = movie.runtime
        binding.yearTextView.text = movie.year
        binding.directorTextView.text = movie.director
        binding.plotTextView.text = movie.plot
    }

    private fun fetchMovie(id: String) {
        val service = RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Replace with the correct method for fetching movie by ID
                val response = service.fetchMoviesByTitle(id, "fb7aca4")

                if (response.response == "True") {
                    movie = response.movie

                    withContext(Dispatchers.Main) {
                        loadData()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MovieDetailActivity, "Movie not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MovieDetailActivity, "Error fetching movie data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
