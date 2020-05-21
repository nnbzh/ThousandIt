package com.example.themoviedb.model.MovieClasses

import com.example.themoviedb.model.MovieClasses.Genre
import com.google.gson.annotations.SerializedName

data class Genres (
    @SerializedName("genres") val genres: List<Genre>
)