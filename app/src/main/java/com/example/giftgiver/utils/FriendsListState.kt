package com.example.giftgiver.utils

import android.os.Parcelable
import com.example.giftgiver.domain.entities.User
import kotlinx.parcelize.Parcelize

@Parcelize
class FriendsListState(val friendsList: List<User>) : Parcelable
