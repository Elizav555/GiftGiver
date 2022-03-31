package com.example.giftgiver.features.user.data.vk

import com.example.giftgiver.features.user.data.vk.entities.UserInfoVk
import com.example.giftgiver.features.user.data.vk.entities.UserVk
import com.example.giftgiver.features.user.domain.UserInfo

class VkMapper {
    fun mapUser(userVk: UserVk): UserInfo = UserInfo(
        vkId = userVk.vkId,
        name = userVk.name,
        photo = userVk.photo,
        bdate = userVk.bdate
    )

    fun mapUserWithInfo(userVk: UserVk, userInfoVk: UserInfoVk): UserInfo {
        val user = UserInfo(
            vkId = userVk.vkId,
            name = userVk.name,
            photo = userVk.photo,
            bdate = userInfoVk.bdate,
            about = userInfoVk.about,
        )
        userInfoVk.photoMax?.let { user.photo = it }
        return user
    }
}