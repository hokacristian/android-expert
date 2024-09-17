package com.hoka.expertsubmission.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoka.core.ui.StoryAdapter
import com.hoka.expertsubmission.detail.DetailActivity
import com.hoka.expertsubmission.favorite.databinding.ActivityFavoriteBinding
import com.hoka.expertsubmission.viewmodel.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.mtAppbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val storyAdapter = StoryAdapter()
        storyAdapter.onItemClick = { storyData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, storyData)
            startActivity(intent)
        }

        favoriteViewModel.favoriteStory.observe(this) { favoriteStory ->
            storyAdapter.setData(favoriteStory)
            binding.tvEmpty.visibility = if (favoriteStory.isNotEmpty()) View.GONE else View.VISIBLE
        }

        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }
}