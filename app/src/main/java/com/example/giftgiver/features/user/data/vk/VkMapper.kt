package com.example.giftgiver.features.user.data.vk

import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.user.data.vk.entities.UserInfoVk
import com.example.giftgiver.features.user.data.vk.entities.UserVk
import com.example.giftgiver.features.user.domain.UserInfo
import javax.inject.Inject

class VkMapper @Inject constructor(private val dateMapper: DateMapper) {
    fun mapUser(userVk: UserVk): UserInfo = UserInfo(
        vkId = userVk.vkId,
        name = userVk.name,
        photo = userVk.photo,
        bdate = dateMapper.formatDateString(userVk.bdate)
    )

    fun mapUserWithInfo(userVk: UserVk, userInfoVk: UserInfoVk): UserInfo {
        val user = UserInfo(
            vkId = userVk.vkId,
            name = userVk.name,
            photo = userVk.photo,
            bdate = dateMapper.formatDateString(userInfoVk.bdate),
            about = userInfoVk.about,
        )
        userInfoVk.photoMax?.let { user.photo = it }
        return user
    }
}
