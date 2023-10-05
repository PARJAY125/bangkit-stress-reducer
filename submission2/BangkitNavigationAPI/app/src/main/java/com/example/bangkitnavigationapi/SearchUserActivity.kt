package com.example.bangkitnavigationapi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangkitnavigationapi.adapter.GithubUserAdapter
import com.example.bangkitnavigationapi.data.Result
import com.example.bangkitnavigationapi.data.dataStore
import com.example.bangkitnavigationapi.databinding.ActivityMainBinding
import com.example.bangkitnavigationapi.utils.Tools.Companion.initAppThemeViewModel
import com.example.bangkitnavigationapi.utils.Tools.Companion.initRetrofitVM
import com.example.bangkitnavigationapi.utils.Tools.Companion.initRoomViewModel
import com.example.bangkitnavigationapi.viewmodel.AppThemeViewModel
import com.example.bangkitnavigationapi.viewmodel.RetrofitViewModel
import com.example.bangkitnavigationapi.viewmodel.RoomViewModel

class SearchUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofitViewModel: RetrofitViewModel
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var githubUserAdapter: GithubUserAdapter
    private lateinit var appThemeViewModel : AppThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ApiService
        retrofitViewModel = initRetrofitVM(this, this)
        roomViewModel = initRoomViewModel(this, this)
        appThemeViewModel = initAppThemeViewModel(application.dataStore, this)

        appThemeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            val menu = binding.sbSearchUser.menu[0]
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menu.icon = ContextCompat.getDrawable(this, R.drawable.ic_bulan)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                menu.icon = ContextCompat.getDrawable(this, R.drawable.ic_matahari)
            }
        }

        binding.sbSearchUser.inflateMenu(R.menu.option_menu)
        binding.sbSearchUser.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    appThemeViewModel.saveThemeSetting()
                    true
                }
                R.id.menu2 -> {
                    startActivity(Intent(this, FavoritedUserActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // TODO | question : the UI isn't auto update, what to do?
        githubUserAdapter = GithubUserAdapter (
            onFavoriteClick = { githubUserItemEntity ->
                if (githubUserItemEntity.isFavorited) roomViewModel.deleteGithubUser(githubUserItemEntity)
                else roomViewModel.saveGithubUser(githubUserItemEntity)
            },
            onItemClick = { username ->
                startActivity(Intent(this, DetailUserActivity::class.java)
                        .putExtra("username", username)
                )
            }
        )

        binding.rvGithubUser.apply {
            layoutManager = LinearLayoutManager(this@SearchUserActivity)
            adapter = githubUserAdapter
        }

        with(binding) {
            svSearchUser.setupWithSearchBar(sbSearchUser)
            svSearchUser
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    sbSearchUser.text = svSearchUser.text
                    svSearchUser.hide()
                    performUserSearch(svSearchUser.text.toString())
                    false
                }
        }

        retrofitViewModel.searchedGithubUserLiveData.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val githubUserData = result.data
                        githubUserAdapter.submitList(githubUserData)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            this, "Terjadi kesalahan" + result.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun performUserSearch(query: String) {
        showLoading(true)
        retrofitViewModel.searchGithubUser(query)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }
}