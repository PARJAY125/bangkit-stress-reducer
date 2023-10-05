package com.example.bangkitnavigationapi.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bangkitnavigationapi.data.GithubUserRepository
import com.example.bangkitnavigationapi.utils.Injection

class RoomViewModelFactory (private val githubUserRepository: GithubUserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            return RoomViewModel(githubUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RoomViewModelFactory? = null
        fun getInstance(context: Context): RoomViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RoomViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}