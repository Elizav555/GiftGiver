package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.vk.entities.UserInfoVk
import com.example.giftgiver.data.vk.entities.UserVk
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.domain.entities.UserInfo

class VkMapper {
    fun mapUser(userVk: UserVk): User = User(
        vkId = userVk.vkId,
        name = userVk.name,
        photo = userVk.photo,
        info = null
    )

    fun mapUserWithInfo(userVk: UserVk, userInfoVk: UserInfoVk): User {
        val info =
            UserInfo(userInfoVk.vkId, userInfoVk.bdate, userInfoVk.about, userInfoVk.photoMax)
        val user = mapUser(userVk)
        user.info = info
        return user
    }
}
