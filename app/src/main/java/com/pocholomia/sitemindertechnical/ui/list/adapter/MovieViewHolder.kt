package com.pocholomia.sitemindertechnical.ui.list.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pocholomia.sitemindertechnical.R
import com.pocholomia.sitemindertechnical.databinding.ItemMovieBinding
import com.pocholomia.sitemindertechnical.domain.Movie

class MovieViewHolder(
    private val binding: ItemMovieBinding,
    private val onClick: (Movie) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie?) {
        binding.root.setOnClickListener {
            movie?.let { onClick(it) }
        }
        itemView.isVisible = movie != null
        movie?.let { showData(it) }
    }

    private fun showData(movie: Movie) {
        Glide.with(itemView)
            .load(movie.poster)
            .fitCenter()
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_image_24)
            .into(binding.imgPoster)
        binding.txtTitle.text = movie.title
        binding.txtYear.text = movie.year
    }

}