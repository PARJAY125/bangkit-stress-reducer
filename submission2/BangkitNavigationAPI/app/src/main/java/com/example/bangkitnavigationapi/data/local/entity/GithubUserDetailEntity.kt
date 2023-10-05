package com.example.bangkitnavigationapi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detailGithubUser")
data class GithubUserDetailEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo("login")
    val username: String,
    @ColumnInfo("avatar_url")
    val avatarUrl: String,
    @ColumnInfo("followers_url")
    val followersUrl: String,
    @ColumnInfo("following_url")
    val followingUrl: String,

    // Detail User
    @ColumnInfo("name")
    val name: String?,
    @ColumnInfo("followers")
    val followersCount: Int?,
    @ColumnInfo("following")
    val followingCount: Int?,

    // isFavorited
    @ColumnInfo("isFavorited")
    var isFavorited: Boolean
)