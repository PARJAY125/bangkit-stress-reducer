package com.example.bangkitnavigationapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bangkitnavigationapi.data.AppThemeSettingPreferences
import kotlinx.coroutines.launch

class AppThemeViewModel (private val pref: AppThemeSettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting() {
        viewModelScope.launch {
            pref.saveThemeSetting()
        }
    }
}