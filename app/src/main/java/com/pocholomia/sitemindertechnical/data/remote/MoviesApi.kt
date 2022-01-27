package com.pocholomia.sitemindertechnical.data.remote

import com.pocholomia.sitemindertechnical.domain.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("?apikey=dfc0e9a0&type=movie")
    suspend fun getMovies(
        @Query("s") search: String,
        @Query("page") page: Int
    ): MovieListResponse

    @GET("?apikey=dfc0e9a0&plot=full")
    suspend fun getMovie(@Query("i") id: String): Movie

}