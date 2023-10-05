package com.example.bangkitnavigationapi.utils

import android.content.Context
import com.example.bangkitnavigationapi.data.GithubUserRepository
import com.example.bangkitnavigationapi.data.local.room.GithubUserDatabase
import com.example.bangkitnavigationapi.data.remote.retrofit.ApiUtilities

object Injection {
    fun provideRepository(context: Context): GithubUserRepository {
        val apiService = ApiUtilities.getApiService()
        val database = GithubUserDatabase.getInstance(context)
        val userListDao = database.userListDao()
        val userDetailDao = database.userDetailDao()
        val appExecutors = AppExecutor()
        return GithubUserRepository.getInstance(apiService, userListDao, userDetailDao, appExecutors)
    }
}