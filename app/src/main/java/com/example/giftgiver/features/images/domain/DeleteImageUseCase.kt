package com.example.giftgiver.features.images.domain

import com.example.giftgiver.utils.AppDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val imageStorage: ImageStorage,
    private val dispatchers: AppDispatchers
) {
    suspend operator fun invoke(url: String) =
        withContext(dispatchers.io) { imageStorage.deleteImage(url) }
}