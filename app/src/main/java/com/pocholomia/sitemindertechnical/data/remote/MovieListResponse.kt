package com.pocholomia.sitemindertechnical.data.remote

import com.pocholomia.sitemindertechnical.domain.Movie
import com.squareup.moshi.Json

data class MovieListResponse(
    @field:Json(name = "Search") val items: List<Movie> = emptyList()
)