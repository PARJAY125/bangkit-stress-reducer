package com.example.bangkitnavigationapi.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bangkitnavigationapi.data.local.entity.GithubUserDetailEntity
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity

@Dao
interface UserListDao {
    @Query("SELECT * FROM tempGithubUserList")
    fun getAllGithubUser(): LiveData<List<GithubUserItemEntity>>

    @Query("SELECT * FROM tempGithubUserList WHERE isFollower = 1")
    suspend fun getGithubUserFollower(): List<GithubUserItemEntity>

    @Query("SELECT EXISTS(SELECT * FROM tempGithubUserList WHERE id = :id)")
    fun isUserAvailable(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM tempGithubUserList WHERE id = :id AND isFavorited = 1)")
    suspend fun isUserFavorited(id: Int): Boolean

    @Query("SELECT * FROM tempGithubUserList WHERE isFollowing = 1")
    fun getGithubUserFollowing(): LiveData<List<GithubUserItemEntity>>

    @Query("SELECT * FROM tempGithubUserList WHERE isFavorited = 1")
    fun getAllFavoritedGithubUser(): LiveData<List<GithubUserItemEntity>>

    @Query("SELECT * FROM tempGithubUserList WHERE login LIKE :username || '%'")
    fun searchGithubUser(username : String): LiveData<List<GithubUserItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGithubUserList(favoritedUser: List<GithubUserItemEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGithubUser(favoritedUser: GithubUserItemEntity)

    @Update
    suspend fun updateUser(favoritedUser: GithubUserItemEntity)

    @Query("DELETE FROM tempGithubUserList WHERE isFavorited = 0")
    suspend fun deleteAllUnfavoritedList()
}