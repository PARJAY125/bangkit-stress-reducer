package com.example.bangkitnavigationapi.data

import androidx.lifecycle.LiveData
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity
import com.example.bangkitnavigationapi.data.local.room.UserDetailDao
import com.example.bangkitnavigationapi.data.local.room.UserListDao
import com.example.bangkitnavigationapi.data.remote.response.GithubUserItem
import com.example.bangkitnavigationapi.data.remote.retrofit.ApiService
import com.example.bangkitnavigationapi.utils.AppExecutor
import com.example.bangkitnavigationapi.utils.Tools

class GithubUserRepository (
    private val apiService: ApiService,
    private val userListDao: UserListDao,
    private val userDetailDao: UserDetailDao,
    private val appExecutors: AppExecutor
) {
    private val token = "token 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"

    suspend fun searchGithubUser(query : String) : Result<List<GithubUserItemEntity>> {
        return try {
            val result = apiService.searchUsers(query, token)
            if (!result.isSuccessful)
                return Result.Error("Response not successful: ${result.code()} ${result.message()}")

            val searchGithubUserListResponse =
                result.body() ?: return Result.Error("Response body is null")

            val githubUserList = ArrayList<GithubUserItemEntity>()
            searchGithubUserListResponse.items.forEach { searchGithubUserResponse ->
                val isFavorited = userListDao.isUserFavorited(searchGithubUserResponse.id)
                val githubUser = Tools.retrofitToRoomUserItem(searchGithubUserResponse, isFavorited, false, false)
                githubUserList.add(githubUser)
            }

            userListDao.deleteAllUnfavoritedList()
            userListDao.insertGithubUserList(githubUserList)

            // TODO : Experimental
            // val localData = userListDao.searchGithubUser(query)
            return Result.Success(githubUserList)
        } catch (e: Exception) {
            Result.Error("An error occurred: ${e.message}")
        }
    }

/*
//    fun searchRoomGithubUser(query : String): LiveData<Result<List<GithubUserItemEntity>>> {
//        searchResult.value = Result.Loading
//        val client = apiService.searchUsers(query, token)
//        client.enqueue(object : Callback<SearchGithubUserResponse> {
//            override fun onResponse(call: Call<SearchGithubUserResponse>, response: Response<SearchGithubUserResponse>) {
//                if (response.isSuccessful) {
//                    val githubUsers = response.body()?.items
//                    val newsList = ArrayList<GithubUserItemEntity>()
//
//                    // TODO | question : is this going to replace the existing (followerCount % followingCount) data
//                    appExecutors.diskIO.execute {
//                        githubUsers?.forEach { githubUser ->
//                            val isFavorited = userDetailDao.isUserFavorited(githubUser.id)
//                            val news = Tools.retrofitToRoomUserItem(githubUser, isFavorited, false, false)
//                            newsList.add(news)
//                        }
//                        userListDao.deleteAllUnfavoritedList()
//                        userListDao.insertGithubUserList(newsList)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<SearchGithubUserResponse>, t: Throwable) {
//                searchResult.value = Result.Error(t.message.toString())
//            }
//        })
//        val localData = userListDao.searchGithubUser(query)
//        searchResult.addSource(localData) { newGithubUserDataList: List<GithubUserItemEntity> ->
//            searchResult.value = Result.Success(newGithubUserDataList)
//        }
//        return searchResult
//    }
*/

    suspend fun detailGithubUser(query : String): GithubUserItem? {
        return try {
            val result = apiService.getUserDetails(query, token)

            if (result.isSuccessful) result.body()
            else null
        } catch (e : Exception) {
            null
        }
    }

    suspend fun getGithubUserConnectionList(username : String, action : Int) : Result<List<GithubUserItemEntity>> {
        return try {
            val result = when (action) {
                GET_FOLLOWING_LIST -> apiService.getFollowersList(username, token)
                GET_FOLLOWERS_LIST -> apiService.getFollowingList(username, token)
                else -> {return Result.Error("Unknow action : $action")}
            }

            if (!result.isSuccessful)
                return Result.Error("Response not successful: ${result.code()} ${result.message()}")

            val followersGithubUserListResponse =
                result.body() ?: return Result.Error("Response body is null")

            val githubUserList = ArrayList<GithubUserItemEntity>()
            followersGithubUserListResponse.forEach { searchGithubUserResponse ->
                val isFavorited = userListDao.isUserFavorited(searchGithubUserResponse.id)
                val githubUser = when (action) {
                    GET_FOLLOWERS_LIST -> Tools.retrofitToRoomUserItem(searchGithubUserResponse, isFavorited, true, false)
                    GET_FOLLOWING_LIST -> Tools.retrofitToRoomUserItem(searchGithubUserResponse, isFavorited, false, true)
                    else -> {return Result.Error("Unknow action : $action")}
                }
                githubUserList.add(githubUser)
            }
            userListDao.deleteAllUnfavoritedList()
            userListDao.insertGithubUserList(githubUserList)

            val localData = userListDao.getGithubUserFollower()
            return Result.Success(localData)
        } catch (e: Exception) {
            Result.Error("An error occurred: ${e.message}")
        }
    }

    fun getFavoritedGithubUserList(): LiveData<List<GithubUserItemEntity>> {
        return userListDao.getAllFavoritedGithubUser()
    }

    fun searchGithubUserList(query: String): LiveData<List<GithubUserItemEntity>> {
        return userListDao.searchGithubUser(query)
    }

    suspend fun setFavoriteStateGithubUser(githubUserItemEntity: GithubUserItemEntity, favoriteState: Boolean) {
        githubUserItemEntity.isFavorited = favoriteState

        if (userListDao.isUserAvailable(githubUserItemEntity.id)) userListDao.updateUser(githubUserItemEntity)
        else userListDao.insertGithubUser(githubUserItemEntity)
    }

    companion object {
        const val GET_FOLLOWERS_LIST = 1
        const val GET_FOLLOWING_LIST = 2

        @Volatile
        private var instance: GithubUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userListDao: UserListDao,
            userDetailDao: UserDetailDao,
            appExecutors: AppExecutor
        ): GithubUserRepository =
            instance ?: synchronized(this) {
                instance ?: GithubUserRepository(apiService, userListDao, userDetailDao, appExecutors)
            }.also { instance = it }
    }
}