package com.hoka.expertsubmission.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
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
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var tvMemoryLeak: TextView

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
                when (story) {
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

        tvMemoryLeak = binding.tvMemoryLeak
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                val splitInstallManager = SplitInstallManagerFactory.create(this)
                val moduleName = "favorite"

                if (splitInstallManager.installedModules.contains(moduleName)) {
                    startFavoriteActivity()
                } else {
                    val request = SplitInstallRequest.newBuilder()
                        .addModule(moduleName)
                        .build()

                    splitInstallManager.startInstall(request)
                        .addOnSuccessListener {
                            startFavoriteActivity()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to install module", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
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

    private fun startFavoriteActivity() {
        try {
            val activityClass = Class.forName("com.hoka.storyapp.favorite.FavoriteActivity")
            startActivity(Intent(this, activityClass))
            Toast.makeText(this, "Menu Favorit", Toast.LENGTH_SHORT).show()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to start activity", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    private fun registerBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        tvMemoryLeak.visibility = View.GONE
                        binding.rvStory.visibility = View.VISIBLE
                    }

                    Intent.ACTION_POWER_DISCONNECTED -> {
                        tvMemoryLeak.visibility = View.VISIBLE
                        binding.rvStory.visibility = View.GONE
                        tvMemoryLeak.text = getString(R.string.power_disconnected)
                    }
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(broadcastReceiver, intentFilter)
    }
}