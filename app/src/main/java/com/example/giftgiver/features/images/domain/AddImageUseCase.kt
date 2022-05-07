package com.example.giftgiver.features.images.domain

import java.io.File
import javax.inject.Inject

class AddImageUseCase @Inject constructor(private val imageStorage: ImageStorage) {
    suspend operator fun invoke(file: File) = imageStorage.addImage(file)
}