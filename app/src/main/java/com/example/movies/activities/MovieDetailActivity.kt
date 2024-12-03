package com.example.movies.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.movies.data.entities.Movie
import com.example.movies.databinding.ActivityMovieDetailBinding
import com.example.movies.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"
    }

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra(EXTRA_MOVIE_ID)
        if (id != null) {
            lifecycleScope.launch {
                getMovieDetails(id)
            }
        } else {
            Log.e("MovieDetailActivity", "Movie ID not found in the intent")
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
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
        binding.directorTextView.text = "Directed by ${movie.director}"
        binding.genreTextView.text = "Genres: ${movie.genre}"
        binding.plotTextView.text = movie.plot
    }

    private suspend fun getMovieDetails(imdbId: String) {
        val service = RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            binding.loadingProgressBar.visibility = View.VISIBLE
        }

        try {
            val response = service.fetchMovieById(imdbId, "fb7aca4")

            if (response.response == "True") {
                movie = Movie(
                    imdbID = response.imdbID,
                    title = response.title,
                    year = response.year,
                    poster = response.poster,
                    plot = response.plot,
                    runtime = response.runtime,
                    director = response.director,
                    genre = response.genre,
                    country = response.country
                )

                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                    binding.loadingProgressBar.visibility = View.GONE
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@MovieDetailActivity, "Movie not found", Toast.LENGTH_SHORT).show()
                    binding.loadingProgressBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            Log.e("APIError", e.stackTraceToString())
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@MovieDetailActivity, "Error fetching movie data", Toast.LENGTH_SHORT).show()
                binding.loadingProgressBar.visibility = View.GONE
            }
        }
    }
}

