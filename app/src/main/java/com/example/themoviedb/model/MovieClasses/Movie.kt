package com.example.themoviedb.model.MovieClasses

import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("id") var id: Int = 0,
    @SerializedName("vote_count") var voteCount: Int = 0,
    @SerializedName("title") var title: String = "",
    @SerializedName("vote_average") var voteAverage: Double = 0.0,
    @SerializedName("poster_path") var posterPath: String = "",
    @SerializedName("release_date") var releaseDate: String = "",
    @SerializedName("popularity") var popularity: String = "",
    @SerializedName("runtime") var runtime: Int = 0,
    @SerializedName("budget") var budget: Int = 0,
    @SerializedName("revenue") var revenue: Int = 0,
    @SerializedName("overview") var overview: String = "",

    @SerializedName("production_companies") var producers: List<Producers>? = null,

    @SerializedName("genres") var genres: List<Genre>? = null,

    var position: Int = 0,
    var isClicked: Boolean = false,
    var genreNames: String = "",
    var producersName: String = ""
)

