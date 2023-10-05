package com.example.bangkitnavigationapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bangkitnavigationapi.data.GithubUserRepository
import com.example.bangkitnavigationapi.data.Result
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(private val githubUserRepository: GithubUserRepository) : ViewModel() {
    fun getFavoritedUser() = githubUserRepository.getFavoritedGithubUserList()

    private val _searchedGithubUserLiveData = MutableLiveData<List<GithubUserItemEntity>>()
    val searchedGithubUserLiveData: LiveData<List<GithubUserItemEntity>> = _searchedGithubUserLiveData

    fun searchGithubUser(query : String) {
        viewModelScope.launch (Dispatchers.IO) {
            val response = githubUserRepository.searchGithubUserList(query)
            _searchedGithubUserLiveData.postValue(response.value)
        }
    }

    fun saveGithubUser(githubUser: GithubUserItemEntity) {
        viewModelScope.launch (Dispatchers.IO) {
            githubUserRepository.setFavoriteStateGithubUser(githubUser, true)
        }
    }

    fun deleteGithubUser(githubUser: GithubUserItemEntity) {
        viewModelScope.launch (Dispatchers.IO) {
            githubUserRepository.setFavoriteStateGithubUser(githubUser, false)
        }
    }
}