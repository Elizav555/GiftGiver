package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.vk.entities.UserInfoVk
import com.example.giftgiver.data.vk.entities.UserVk
import com.example.giftgiver.domain.entities.UserInfo

class VkMapper {
    fun mapUser(userVk: UserVk): UserInfo = UserInfo(
        vkId = userVk.vkId,
        name = userVk.name,
        photo = userVk.photo,
    )

    fun mapUserWithInfo(userVk: UserVk, userInfoVk: UserInfoVk) = UserInfo(
        vkId = userVk.vkId,
        name = userVk.name,
        photo = userVk.photo,
        bdate = userInfoVk.bdate,
        about = userInfoVk.about,
        photoMax = userInfoVk.photoMax,
    )
}
