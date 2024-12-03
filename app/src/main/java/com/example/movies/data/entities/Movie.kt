package com.example.movies.data.entities

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("imdbID") var imdbID: String,
    @SerializedName("Search") val search: List<Movie>,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String?,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Plot") val plot: String,
    @SerializedName("Runtime") val runtime: String?,
    @SerializedName("Director") val director: String,
    @SerializedName("Genre") val genre: String,
    @SerializedName("Country") val country: String,
    @SerializedName("Response") val response: String
)

data class Movie (
    @SerializedName("imdbID") var imdbID: String,
    @SerializedName("Title") var title:String,
    @SerializedName("Year") var year:String?,
    @SerializedName("Poster") var poster:String,
    @SerializedName("Plot") var plot:String,
    @SerializedName("Runtime") var runtime:String?,
    @SerializedName("Director") var director:String,
    @SerializedName("Genre") var genre:String,
    @SerializedName("Country") var country:String,
)