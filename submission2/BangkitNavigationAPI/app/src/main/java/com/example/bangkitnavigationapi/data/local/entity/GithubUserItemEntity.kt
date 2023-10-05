package com.example.bangkitnavigationapi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tempGithubUserList")
data class GithubUserItemEntity(
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

    // isFavorited
    @ColumnInfo("isFavorited")
    var isFavorited: Boolean,

    @ColumnInfo("isFollower")
    var isFollower: Boolean?,
    @ColumnInfo("isFollowing")
    var isFollowing: Boolean?
)