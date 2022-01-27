package com.pocholomia.sitemindertechnical.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocholomia.sitemindertechnical.domain.Movie
import com.pocholomia.sitemindertechnical.domain.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    data class UiState(
        val movie: Movie? = null,
        val loading: Boolean = false,
        val error: Throwable? = null
    )

    private var fetchJob: Job? = null

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getMovieDetails(id: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                _uiState.update { it.copy(loading = true) }
                val movie = movieUseCase.getMovieDetails(id)
                _uiState.update { UiState(movie = movie) }
            } catch (e: Throwable) {
                _uiState.update { it.copy(error = e, loading = false) }
            }
        }
    }

}