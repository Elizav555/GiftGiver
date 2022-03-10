package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.UserInfoR

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(userInfo: UserInfoR)

    @Update
    fun updateUsersInfo(userInfo: UserInfoR)

    @Query("SELECT * FROM users_info")
    fun getUserInfo(): MutableList<UserInfoR>

    @Query("SELECT * FROM users_info WHERE vkId = :vkId")
    fun getUserInfoByVkId(vkId: Long): UserInfoR?

    @Delete
    fun deleteUserInfo(userInfo: UserInfoR)

    @Query("DELETE FROM users_info")
    fun deleteAllUsersInfo()
}
