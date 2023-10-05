package com.example.bangkitnavigationapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bangkitnavigationapi.data.GithubUserRepository
import com.example.bangkitnavigationapi.data.Result
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity
import com.example.bangkitnavigationapi.data.remote.response.GithubUserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RetrofitViewModel (private val githubUserRepository: GithubUserRepository) :ViewModel() {

    private val _searchedGithubUserLiveData = MutableLiveData<Result<List<GithubUserItemEntity>>>()
    val searchedGithubUserLiveData: LiveData<Result<List<GithubUserItemEntity>>> = _searchedGithubUserLiveData

    fun searchGithubUser(query : String) {
        viewModelScope.launch (Dispatchers.IO) {
            val response = githubUserRepository.searchGithubUser(query)
            _searchedGithubUserLiveData.postValue(response)
        }
    }

    private val _githubDetailUserLiveData = MutableLiveData<GithubUserItem>()
    val githubDetailUserLiveData: LiveData<GithubUserItem> = _githubDetailUserLiveData

    fun detailGithubUser(username : String) {
        viewModelScope.launch (Dispatchers.IO) {
            val response = githubUserRepository.detailGithubUser(username)
            _githubDetailUserLiveData.postValue(response!!)
        }
    }

    private val _githubUserFollowerLiveData = MutableLiveData<Result<List<GithubUserItemEntity>>>()
    val githubUserFollowerLiveData: LiveData<Result<List<GithubUserItemEntity>>> = _githubUserFollowerLiveData
    private val _githubUserFollowingLiveData = MutableLiveData<Result<List<GithubUserItemEntity>>>()
    val githubUserFollowingLiveData: LiveData<Result<List<GithubUserItemEntity>>> = _githubUserFollowingLiveData

    fun getGithubUserConnectionList(username : String, action : Int) {
         if (action == GithubUserRepository.GET_FOLLOWING_LIST) viewModelScope.launch (Dispatchers.IO) {
             val response = githubUserRepository.getGithubUserConnectionList(username, GithubUserRepository.GET_FOLLOWING_LIST)
             _githubUserFollowingLiveData.postValue(response)
         }
         else if (action == GithubUserRepository.GET_FOLLOWERS_LIST) viewModelScope.launch (Dispatchers.IO) {
             val response = githubUserRepository.getGithubUserConnectionList(username, GithubUserRepository.GET_FOLLOWERS_LIST)
             _githubUserFollowerLiveData.postValue(response)
         }
     }
}