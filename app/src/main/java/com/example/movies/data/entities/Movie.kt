package com.example.movies.data.entities

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("Search") val search: List<Movie>,
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
    @SerializedName("Response") val response: String
)