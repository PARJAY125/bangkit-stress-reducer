package com.example.bangkitnavigationapi

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bangkitnavigationapi.databinding.ActivityDetailUserBinding
import com.example.bangkitnavigationapi.fragment.SectionsPagerAdapter
import com.example.bangkitnavigationapi.utils.Tools.Companion.initRetrofitVM
import com.example.bangkitnavigationapi.viewmodel.RetrofitViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    companion object {
        private val TAB_TITLES = arrayListOf(
            "Followers",
            "Following"
        )
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var retrofitViewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val username = intent.getStringExtra("username")!!
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        binding.vpTab.adapter = sectionsPagerAdapter

        // Initialize ApiService
        retrofitViewModel = initRetrofitVM(this, this)

        retrofitViewModel.githubDetailUserLiveData.observe(this) {
            Glide.with(binding.root)
                .load(it.avatarUrl)
                .into(binding.ivProfilePicture)

            binding.tvUsername.text = it.login
            binding.tvName.text = it.name
            TAB_TITLES[0] = "Followers : ${it.followersCount}"
            TAB_TITLES[1] = "Following : ${it.followingCount}"

            TabLayoutMediator(binding.tabLayout, binding.vpTab) { tab, position ->
                tab.text = TAB_TITLES[position]
            }.attach()
            supportActionBar?.elevation = 0f
            showLoading(false)
        }

        retrofitViewModel.detailGithubUser(username)

//        retrofitViewModel.detailGithubUser(username).observe(this) { result ->
//            if (result != null) {
//                when (result) {
//                    is Result.Loading -> showLoading(true)
//                    is Result.Success -> {
//                        showLoading(false)
//
//                        val githubUserData = result.data
//                        Glide.with(binding.root)
//                            .load(githubUserData.avatarUrl)
//                            .into(binding.ivProfilePicture)
//
//                        binding.tvUsername.text = githubUserData.username
//                        binding.tvName.text = githubUserData.name
//                        TAB_TITLES[0] = "Followers : ${githubUserData.followersCount}"
//                        TAB_TITLES[1] = "Following : ${githubUserData.followingCount}"
//
//                        TabLayoutMediator(binding.tabLayout, binding.vpTab) { tab, position ->
//                            tab.text = TAB_TITLES[position]
//                        }.attach()
//                        supportActionBar?.elevation = 0f
//                    }
//                    is Result.Error -> {
//                        showLoading(false)
//                        Toast.makeText(
//                            this, "Terjadi kesalahan" + result.error, Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//        }

//        TabLayoutMediator(binding.tabLayout, binding.vpTab) { tab, position ->
//            tab.text = TAB_TITLES[position]
//        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }
}