package com.example.giftgiver.features.images.domain

import android.net.Uri
import java.io.File

interface ImageStorage {
    suspend fun addImage(newFile: File): Uri?
    suspend fun getDefaultUrl(): Uri?
    suspend fun deleteImage(url: String)
}
