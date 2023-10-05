package com.example.bangkitnavigationapi.data.remote.response

import com.google.gson.annotations.SerializedName

data class GithubUserItem(
    val id: Int,
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("followers_url")
    val followersUrl: String,
    @SerializedName("following_url")
    val followingUrl: String,

    @SerializedName("name")
    val name: String?,
    @SerializedName("followers")
    val followersCount: Int?,
    @SerializedName("following")
    val followingCount: Int?
)