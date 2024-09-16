package com.hoka.expertsubmission.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.hoka.core.domain.model.Story
import com.hoka.expertsubmission.R
import com.hoka.expertsubmission.databinding.ActivityDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.mtAppbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        @Suppress("DEPRECATION") val detailStory = intent.getParcelableExtra<Story>(EXTRA_DATA)

        detailStory.let { story ->
            binding.tvItemName.text = story?.name
            binding.tvItemDescription.text = story?.description

            Glide.with(this)
                .load(story?.photoUrl)
                .into(binding.ivItemImage)

            var statusFavorite = story?.isFavorite

            if (statusFavorite != null) {
                setStatusFavorite(statusFavorite)
            }

            binding.fabFavorite.setOnClickListener {
                statusFavorite = !statusFavorite!!
                if (detailStory != null) {
                    detailViewModel.setFavoriteStory(detailStory, statusFavorite!!)
                    setStatusFavorite(statusFavorite!!)
                }
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_favorite_24))
        } else {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_favorite_border_24))
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}