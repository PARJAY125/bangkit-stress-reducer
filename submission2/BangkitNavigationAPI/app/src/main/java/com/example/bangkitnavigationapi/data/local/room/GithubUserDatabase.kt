package com.example.bangkitnavigationapi.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bangkitnavigationapi.data.local.entity.GithubUserDetailEntity
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity

@Database(entities = [GithubUserItemEntity::class, GithubUserDetailEntity::class], version = 1, exportSchema = false)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun userListDao(): UserListDao
    abstract fun userDetailDao() : UserDetailDao

    companion object {
        @Volatile
        private var instance: GithubUserDatabase? = null
        fun getInstance(context: Context): GithubUserDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GithubUserDatabase::class.java, "GithubUserDatabase.db"
                ).build()
            }
    }
}