package com.hoka.expertsubmission.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoka.core.auth.UserRepository
import com.hoka.core.data.source.Resource
import com.hoka.core.ui.StoryAdapter
import com.hoka.expertsubmission.MainActivity
import com.hoka.expertsubmission.R
import com.hoka.expertsubmission.databinding.ActivityHomeBinding
import com.hoka.expertsubmission.detail.DetailActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()
    private val userRepository: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyAdapter = StoryAdapter()
        storyAdapter.onItemClick = { storyData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, storyData)
            startActivity(intent)
        }

        homeViewModel.story.observe(this) { story ->
            if (story != null) {
                when(story) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        storyAdapter.setData(story.data)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storyAdapter
        }

        val toolbar = binding.mtAppbar
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                startActivity(Intent(this, Class.forName("com.hoka.expertsubmission.favorite.FavoriteActivity")))
                Toast.makeText(this, "Menu Favorit", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_logout -> {
                userRepository.logoutUser()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}