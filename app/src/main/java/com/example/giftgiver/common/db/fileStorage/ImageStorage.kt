package com.example.giftgiver.common.db.fileStorage

import android.net.Uri
import java.io.File

interface ImageStorage {
    suspend fun addImage(newFile: File): Uri?
    suspend fun getDefaultUrl()
}
