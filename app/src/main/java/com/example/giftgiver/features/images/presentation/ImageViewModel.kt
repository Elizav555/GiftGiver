package com.example.giftgiver.features.images.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.utils.AppDispatchers
import com.example.giftgiver.utils.copyInputStreamToFile
import com.example.giftgiver.utils.correctRotation
import com.example.giftgiver.utils.getTempFile
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageViewModel @Inject constructor(
    private val context: Context,
    private val dispatchers: AppDispatchers
) : ViewModel() {

    private val _imageFileLiveData = MutableLiveData<File?>()
    val imageFileLiveData: LiveData<File?> = _imageFileLiveData

    private val _imageBitmapLiveData = MutableLiveData<Bitmap?>()
    val imageBitmapLiveData: LiveData<Bitmap?> = _imageBitmapLiveData

    fun onCameraImagePicked(cameraImageFile: File) {
        viewModelScope.launch {
            processImageFile(cameraImageFile)
            _imageFileLiveData.value = cameraImageFile
            _imageBitmapLiveData.value = convertFileToBitmap(cameraImageFile)
        }
    }

    fun onGalleryImagePicked(uri: Uri) {
        viewModelScope.launch {
            val file = convertUriToFile(uri)
            _imageFileLiveData.value = file
            _imageBitmapLiveData.value = convertFileToBitmap(file)
        }
    }

    private suspend fun convertUriToFile(uri: Uri): File? {
        return withContext(dispatchers.io) {
            try {
                val fileDeferred = async {
                    val inputStream = context
                        .contentResolver
                        .openInputStream(uri)
                    return@async context
                        .getTempFile().apply {
                            copyInputStreamToFile(inputStream)
                            correctRotation()
                        }
                }
                fileDeferred.await()
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    private suspend fun processImageFile(file: File) {
        withContext(dispatchers.io) {
            file.correctRotation()
        }
    }

    private suspend fun convertFileToBitmap(file: File?): Bitmap? {
        return withContext(dispatchers.io) {
            try {
                return@withContext BitmapFactory.decodeFile(file?.absolutePath)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }
}
