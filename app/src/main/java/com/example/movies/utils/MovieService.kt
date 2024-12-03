package com.example.movies.utils

import com.example.movies.data.entities.Movie
import com.example.movies.data.entities.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    // Search for movies by title
    @GET("/")
    suspend fun fetchMoviesByTitle(
        @Query("s") title: String,     // The search query for movie title
        @Query("apikey") apiKey: String // Your OMDb API key
    ): MovieSearchResponse
}

