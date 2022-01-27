package com.pocholomia.sitemindertechnical.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.pocholomia.sitemindertechnical.R
import com.pocholomia.sitemindertechnical.databinding.FragmentMovieDetailsBinding
import com.pocholomia.sitemindertechnical.domain.Movie
import com.pocholomia.sitemindertechnical.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)
    private val viewModel by viewModels<MovieDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = MovieDetailsFragmentArgs.fromBundle(requireArguments()).id

        viewModel.getMovieDetails(id)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnRetry.setOnClickListener {
            viewModel.getMovieDetails(id)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.layoutContent.isVisible = uiState.movie != null
                    binding.layoutLoading.isVisible = uiState.loading
                    binding.layoutError.isVisible = uiState.error != null && uiState.movie == null
                    uiState.movie?.let(::setMovie)
                    uiState.error?.let { error ->
                        binding.errorMsg.text =
                            error.localizedMessage ?: getString(R.string.msg_unknown_error)
                    }
                }
            }
        }
    }

    private fun setMovie(movie: Movie) {
        Glide.with(this)
            .load(movie.poster)
            .into(binding.imgPoster)
        binding.txtTitle.text = movie.title
        binding.txtYear.text = movie.year
        binding.txtLanguage.text = movie.language
        binding.txtActor.text = movie.actors
        binding.txtDuration.text = movie.runtime
        binding.txtPlot.text = movie.plot
    }

}