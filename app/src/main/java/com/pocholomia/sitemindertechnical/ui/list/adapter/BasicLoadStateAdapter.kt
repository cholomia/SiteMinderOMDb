package com.pocholomia.sitemindertechnical.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.pocholomia.sitemindertechnical.databinding.ItemLoadStateBinding

class BasicLoadStateAdapter(
    private val onRetry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder = LoadStateViewHolder(
        ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onRetry
    )

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

}