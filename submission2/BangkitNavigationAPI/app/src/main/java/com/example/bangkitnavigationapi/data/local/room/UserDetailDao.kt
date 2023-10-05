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
interface UserDetailDao {
    @Query("DELETE FROM detailGithubUser WHERE isFavorited = 0")
    fun deleteAllUnfavoritedDetail()

    @Query("SELECT EXISTS(SELECT * FROM detailGithubUser WHERE id = :id)")
    fun isSaved(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM detailGithubUser WHERE id = :id AND isFavorited = 1)")
    fun isUserFavorited(id: Int): Boolean

    @Query("SELECT * FROM detailgithubuser WHERE isFavorited = 1")
    fun getAllFavoritedGithubUser(): LiveData<List<GithubUserDetailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGithubUserDetail(favoritedUser: GithubUserDetailEntity)

    @Update
    fun updateDetailUser(favoritedUser: GithubUserDetailEntity)

    @Query("SELECT * FROM detailgithubuser WHERE login = :username")
    fun getGithubDetailUserByUsername(username : String): LiveData<GithubUserDetailEntity>
}