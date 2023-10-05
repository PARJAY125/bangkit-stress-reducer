package com.example.bangkitnavigationapi.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bangkitnavigationapi.data.GithubUserRepository
import com.example.bangkitnavigationapi.utils.Injection

class RetrofitViewModelFactory (private val githubUserRepository: GithubUserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetrofitViewModel::class.java)) {
            return RetrofitViewModel(githubUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RetrofitViewModelFactory? = null
        fun getInstance(context: Context): RetrofitViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RetrofitViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}