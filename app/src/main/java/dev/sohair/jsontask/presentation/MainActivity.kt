package dev.sohair.jsontask.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dev.sohair.jsontask.R
import dev.sohair.jsontask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Code
        setUpUI()
        subscribeToObservers()
    }

    private fun setUpUI() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        viewModel.fetchUsers()
        viewModel.fetchPosts()
    }

    private fun subscribeToObservers() {
        viewModel.posts.observe(this) { posts ->
            Log.d("MainActivity", "subscribeToObservers: $posts")
        }

        viewModel.users.observe(this) { users ->
            Log.d("MainActivity", "subscribeToObservers: $users")
        }

        viewModel.itemDataList.observe(this) {
            binding.progressBar.visibility = View.GONE
            Log.d("MainActivity", "combine: $it")
            UiItemsAdapter(it)
            binding.recyclerView.adapter = UiItemsAdapter(it)
        }
    }
}