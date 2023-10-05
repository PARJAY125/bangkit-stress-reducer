package com.example.bangkitnavigationapi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangkitnavigationapi.adapter.GithubUserAdapter
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity
import com.example.bangkitnavigationapi.databinding.ActivityFavoritedUserBinding
import com.example.bangkitnavigationapi.utils.Tools
import com.example.bangkitnavigationapi.viewmodel.RoomViewModel

class FavoritedUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritedUserBinding
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var githubUserAdapter: GithubUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritedUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomViewModel = Tools.initRoomViewModel(this, this)

        // TODO | question : but this UI is Auto update, something must be wrong in vm or repo or dao
        githubUserAdapter = GithubUserAdapter (
            onFavoriteClick = { githubUserItemEntity ->
                if (githubUserItemEntity.isFavorited) roomViewModel.deleteGithubUser(githubUserItemEntity)
                else roomViewModel.saveGithubUser(githubUserItemEntity)
            },
            onItemClick = { username ->
                startActivity(
                    Intent(this, DetailUserActivity::class.java)
                    .putExtra("username", username)
                )
            }
        )

        binding.rvGithubUser.apply {
            layoutManager = LinearLayoutManager(this@FavoritedUserActivity)
            adapter = githubUserAdapter
        }

        showLoading(true)
        roomViewModel.getFavoritedUser().observe(this) {
            val githubUserItemEntity : List<GithubUserItemEntity> = it
            githubUserAdapter.submitList(githubUserItemEntity)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }
}