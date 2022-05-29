package com.example.giftgiver.features.images.domain

import com.example.giftgiver.utils.AppDispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AddImageUseCase @Inject constructor(
    private val imageStorage: ImageStorage,
    private val dispatchers: AppDispatchers
) {
    suspend operator fun invoke(file: File) =
        withContext(dispatchers.io) { imageStorage.addImage(file) }
}