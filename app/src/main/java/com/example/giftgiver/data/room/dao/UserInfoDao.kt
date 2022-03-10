package com.example.giftgiver.data.room.dao

import androidx.room.*
import com.example.giftgiver.data.room.entities.UserInfoR

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(userInfo: UserInfoR)

    @Update
    suspend fun updateUsersInfo(userInfo: UserInfoR)

    @Query("SELECT * FROM users_info")
    suspend fun getUserInfo(): MutableList<UserInfoR>

    @Query("SELECT * FROM users_info WHERE vkId = :vkId")
    suspend fun getUserInfoByVkId(vkId: Long): UserInfoR?

    @Delete
    suspend fun deleteUserInfo(userInfo: UserInfoR)

    @Query("DELETE FROM users_info")
    suspend fun deleteAllUsersInfo()
}
