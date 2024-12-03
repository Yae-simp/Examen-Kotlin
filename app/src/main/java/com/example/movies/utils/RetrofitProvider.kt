package com.example.movies.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {

    //Companion object to make the function static and accessible without needing an instance of RetrofitProvider.
    companion object {

        //Returns a RecipeService instance, which is an interface that defines API calls.
        fun getRetrofit(): MovieService {

            // Creating a Retrofit object using the Retrofit.Builder().
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")  //Base URL of the API, where the API will be accessed.
                .addConverterFactory(GsonConverterFactory.create())  //Adds a converter to serialize/deserialize data (JSON to Kotlin objects).
                .build()  //Builds the Retrofit object.

            //Returns the RecipeService interface, which is used to define the API endpoints.
            return retrofit.create(MovieService::class.java)
        }
    }
}