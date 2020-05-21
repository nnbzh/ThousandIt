package com.example.themoviedb.model.MovieClasses

import com.google.gson.annotations.SerializedName

data class Genre (

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)