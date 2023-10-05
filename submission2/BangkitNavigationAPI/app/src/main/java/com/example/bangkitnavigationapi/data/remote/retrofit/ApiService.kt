package com.example.bangkitnavigationapi.data.remote.retrofit

import com.example.bangkitnavigationapi.data.remote.response.GithubUserItem
import com.example.bangkitnavigationapi.data.remote.response.SearchGithubUserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Search for GitHub users
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Header("Authorization") token: String
    ): Response<SearchGithubUserResponse>

    // Get details of a specific GitHub user
    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Response<GithubUserItem>

    // List followers of a GitHub user
    @GET("users/{username}/followers")
    suspend fun getFollowersList(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Response<List<GithubUserItem>>

    // List users being followed by a GitHub user
    @GET("users/{username}/following")
    suspend fun getFollowingList(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Response<List<GithubUserItem>>
}