package com.pocholomia.sitemindertechnical.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pocholomia.sitemindertechnical.data.remote.MoviesApi
import com.pocholomia.sitemindertechnical.domain.Movie
import timber.log.Timber

class MoviePagingSource(
    private val moviesApi: MoviesApi,
    private val search: String
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(
        state: PagingState<Int, Movie>
    ): Int? = state.anchorPosition?.let { anchorPosition ->
        state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = moviesApi.getMovies(search, page)
            val items = response.items
            val nextKey = if (items.isEmpty()) null else page + (params.loadSize / 10)
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

}