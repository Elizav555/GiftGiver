package com.example.giftgiver.features.images.domain

import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(private val imageStorage: ImageStorage) {
    suspend operator fun invoke(url: String) = imageStorage.deleteImage(url)
}