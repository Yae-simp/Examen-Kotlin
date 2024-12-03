package com.example.movies.utils

import com.example.movies.data.entities.Movie
import com.example.movies.data.entities.MovieDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("/")
    suspend fun fetchMoviesByTitle(
        @Query("s") title: String,
        @Query("apikey") apiKey: String
    ): MovieDetailResponse

    @GET("/")
    suspend fun fetchMovieById(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
    ): Movie
}

