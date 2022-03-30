package com.example.giftgiver.domain.repositories

import android.net.Uri
import java.io.File

interface ImageStorage {
    suspend fun addImage(newFile: File): Uri?
    suspend fun getDefaultUrl()
}
