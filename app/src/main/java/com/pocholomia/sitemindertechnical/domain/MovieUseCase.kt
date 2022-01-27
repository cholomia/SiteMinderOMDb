package com.pocholomia.sitemindertechnical.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {

    fun getMovies(search: String): Flow<PagingData<Movie>>

    suspend fun getMovieDetails(imdbID: String): Movie

}