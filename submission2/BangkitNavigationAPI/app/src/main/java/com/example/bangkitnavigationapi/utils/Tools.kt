package com.example.bangkitnavigationapi.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.bangkitnavigationapi.data.AppThemeSettingPreferences
import com.example.bangkitnavigationapi.data.Result
import com.example.bangkitnavigationapi.data.local.entity.GithubUserDetailEntity
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity
import com.example.bangkitnavigationapi.data.remote.response.GithubUserItem
import com.example.bangkitnavigationapi.viewmodel.AppThemeViewModel
import com.example.bangkitnavigationapi.viewmodel.AppThemeViewModelFactory
import com.example.bangkitnavigationapi.viewmodel.RetrofitViewModel
import com.example.bangkitnavigationapi.viewmodel.RetrofitViewModelFactory
import com.example.bangkitnavigationapi.viewmodel.RoomViewModel
import com.example.bangkitnavigationapi.viewmodel.RoomViewModelFactory

class Tools {
    companion object {

        fun retrofitToRoomUserItem(githubUserItem : GithubUserItem,
                                   isFavorited : Boolean, isFollower : Boolean, isFollowing : Boolean): GithubUserItemEntity {
            return GithubUserItemEntity(
                githubUserItem.id,
                githubUserItem.login,
                githubUserItem.avatarUrl,
                githubUserItem.followersUrl,
                githubUserItem.followingUrl,
                isFavorited,
                isFollower,
                isFollowing
            )
        }

        fun retrofitToRoomUserDetail(githubUserItem: GithubUserItem, isFavorited : Boolean): GithubUserDetailEntity {
            return GithubUserDetailEntity(
                githubUserItem.id,
                githubUserItem.login,
                githubUserItem.avatarUrl,
                githubUserItem.followersUrl,
                githubUserItem.followingUrl,
                githubUserItem.name,
                githubUserItem.followersCount,
                githubUserItem.followingCount,
                isFavorited
            )
        }

        fun initRoomViewModel (context: Context, owner: ViewModelStoreOwner) : RoomViewModel {
            val roomFactory = RoomViewModelFactory.getInstance(context)
            return ViewModelProvider(owner, roomFactory)[RoomViewModel::class.java]
        }

        fun initRetrofitVM (context: Context, owner: ViewModelStoreOwner) : RetrofitViewModel {
            val roomFactory = RetrofitViewModelFactory.getInstance(context)
            return ViewModelProvider(owner, roomFactory)[RetrofitViewModel::class.java]
        }

        fun initAppThemeViewModel (dataStore: DataStore<Preferences>, owner: ViewModelStoreOwner) : AppThemeViewModel {
            val pref = AppThemeSettingPreferences.getInstance(dataStore)
            return ViewModelProvider(owner, AppThemeViewModelFactory(pref))[AppThemeViewModel::class.java]
        }
    }
}