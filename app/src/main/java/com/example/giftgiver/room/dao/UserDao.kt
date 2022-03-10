package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.UserR

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: UserR)

    @Update
    fun updateUsers(user: UserR)

    @Query("SELECT * FROM users")
    fun getUsers(): MutableList<UserR>

    @Query("SELECT * FROM users WHERE vkId = :vkId")
    fun getUserByVkId(vkId: Long): UserR?

    @Delete
    fun deleteUser(user: UserR)

    @Query("DELETE FROM users")
    fun deleteAllUsers()
}
