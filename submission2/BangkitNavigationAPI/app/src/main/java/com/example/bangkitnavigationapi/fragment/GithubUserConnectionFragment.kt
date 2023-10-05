package com.example.bangkitnavigationapi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangkitnavigationapi.adapter.GithubUserAdapter
import com.example.bangkitnavigationapi.data.GithubUserRepository
import com.example.bangkitnavigationapi.data.Result
import com.example.bangkitnavigationapi.data.local.entity.GithubUserDetailEntity
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity
import com.example.bangkitnavigationapi.databinding.FragmentGithubUserConnectionBinding
import com.example.bangkitnavigationapi.utils.Tools
import com.example.bangkitnavigationapi.viewmodel.RetrofitViewModel
import com.example.bangkitnavigationapi.viewmodel.RoomViewModel

class GithubUserConnectionFragment : Fragment() {

    private lateinit var username: String
    private lateinit var action: String

    private lateinit var retrofitViewModel: RetrofitViewModel
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var githubUserAdapter: GithubUserAdapter

    private var _binding: FragmentGithubUserConnectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGithubUserConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(USERNAME).toString()
        action = arguments?.getString("action").toString()

        retrofitViewModel = Tools.initRetrofitVM(requireActivity(), requireActivity())
        roomViewModel = Tools.initRoomViewModel(requireActivity(), requireActivity())

        // TODO | question : the UI isn't auto update, what to do?
        githubUserAdapter = GithubUserAdapter (
            onFavoriteClick = { githubUserItemEntity ->
                if (githubUserItemEntity.isFavorited)
                    roomViewModel.deleteGithubUser(githubUserItemEntity)
                else
                    roomViewModel.saveGithubUser(githubUserItemEntity)
            },
            onItemClick = { }
        )

        binding.rvFollowers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = githubUserAdapter
        }

        if (action == GET_FOLLOWERS) {
            showLoading(true)
            retrofitViewModel.githubUserFollowerLiveData.observe(requireActivity()) { result ->
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
                                requireActivity(), "Terjadi kesalahan" + result.error, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else if (action == GET_FOLLOWING) {
            showLoading(true)
            retrofitViewModel.githubUserFollowingLiveData.observe(requireActivity()) {result ->
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
                                requireActivity(), "Terjadi kesalahan" + result.error, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        retrofitViewModel.getGithubUserConnectionList(username, GithubUserRepository.GET_FOLLOWING_LIST)
        retrofitViewModel.getGithubUserConnectionList(username, GithubUserRepository.GET_FOLLOWERS_LIST)
        showLoading(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val USERNAME = "username"
        const val GET_FOLLOWERS = "followers"
        const val GET_FOLLOWING = "following"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }
}