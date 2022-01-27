package com.pocholomia.sitemindertechnical.ui.list

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pocholomia.sitemindertechnical.R
import com.pocholomia.sitemindertechnical.databinding.FragmentMovieListBinding
import com.pocholomia.sitemindertechnical.ui.list.adapter.BasicLoadStateAdapter
import com.pocholomia.sitemindertechnical.ui.list.adapter.MovieAdapter
import com.pocholomia.sitemindertechnical.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val binding by viewBinding(FragmentMovieListBinding::bind)
    private val viewModel by viewModels<MovieListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.setOnEditorActionListener { _, actionId, _ ->
            doSearch(actionId == EditorInfo.IME_ACTION_GO)
        }
        binding.search.setOnKeyListener { _, keyCode, event ->
            doSearch(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state
                .map { it.query }
                .distinctUntilChanged()
                .collect(binding.search::setText)
        }

        val adapter = MovieAdapter {
            findNavController().navigate(MovieListFragmentDirections.openDetail(it.imdbID))
        }
        binding.btnRetry.setOnClickListener { adapter.retry() }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val footer = BasicLoadStateAdapter { adapter.retry() }
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = footer,
            footer = footer
        )
        binding.swipeRefreshLayout.setOnRefreshListener { adapter.refresh() }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy != 0) {
                    viewModel.accept(MovieListViewModel.UiAction.Scroll(currentQuery = viewModel.state.value.query))
                }
            }
        })

        val notLoading = adapter.loadStateFlow
            .map { it.source.refresh is LoadState.NotLoading }
        val hasNotScrolledForCurrentSearch = viewModel.state
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()
        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.collectLatest(adapter::submitData)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            shouldScrollToTop.collect {
                if (it) {
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                val isError = loadState.refresh is LoadState.Error && adapter.itemCount == 0
                binding.recyclerView.isVisible = !isListEmpty
                binding.layoutEmptyState.isVisible = isListEmpty || isError
                binding.errorMsg.text = if (isError) {
                    (loadState.refresh as? LoadState.Error)?.error?.localizedMessage
                        ?: getString(R.string.msg_unknown_error)
                } else {
                    getString(R.string.msg_empty_list)
                }
                binding.swipeRefreshLayout.isRefreshing = loadState.refresh is LoadState.Loading
            }
        }
    }

    private fun doSearch(condition: Boolean): Boolean {
        return if (condition) {
            binding.search.text?.trim()?.let {
                if (it.isNotEmpty()) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.accept(MovieListViewModel.UiAction.Search(query = it.toString()))
                }
            }
            true
        } else {
            false
        }
    }

}