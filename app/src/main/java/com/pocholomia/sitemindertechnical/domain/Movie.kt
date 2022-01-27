package com.pocholomia.sitemindertechnical.domain

import com.squareup.moshi.Json

data class Movie(
    @field:Json(name = "imdbID") val imdbID: String,
    @field:Json(name = "Title") val title: String,
    @field:Json(name = "Year") val year: String,
    @field:Json(name = "Poster") val poster: String,
    @field:Json(name = "Plot") val plot: String?,
    @field:Json(name = "Language") val language: String?,
    @field:Json(name = "Actors") val actors: String?,
    @field:Json(name = "Runtime") val runtime: String?,
)