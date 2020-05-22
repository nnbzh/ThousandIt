package com.example.themoviedb.model.MovieClasses

import com.google.gson.annotations.SerializedName

class MoviesResponse (
    @SerializedName("results") val movieList: List<Movie>,
    @SerializedName("page") val page: Int
)