package com.pocholomia.sitemindertechnical.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocholomia.sitemindertechnical.data.remote.MoviesApi
import com.pocholomia.sitemindertechnical.domain.Movie
import com.pocholomia.sitemindertechnical.domain.MovieUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieUseCaseImpl @Inject constructor(
    private val moviesApi: MoviesApi
) : MovieUseCase {

    override fun getMovies(search: String): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 10, enablePlaceholders = false)) {
            MoviePagingSource(moviesApi, search)
        }.flow
    }

    override suspend fun getMovieDetails(imdbID: String): Movie {
        return moviesApi.getMovie(imdbID)
    }

}