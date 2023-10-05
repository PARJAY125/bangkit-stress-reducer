package com.example.bangkitnavigationapi.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchGithubUserResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<GithubUserItem>
)