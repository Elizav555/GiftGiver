package com.example.giftgiver.data.firebase

import android.net.Uri
import com.example.giftgiver.domain.repositories.ImageStorage
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ImageStorageImpl : ImageStorage {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val imagesRef = storageRef.child("images")

    override suspend fun addImage(newFile: File): Uri? {
        val newImageRef = imagesRef.child(newFile.name)
        val uploadTask = newImageRef.putFile(Uri.fromFile(newFile))
        return suspendCoroutine { continuation ->
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                newImageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    continuation.resume(downloadUri)
                } else {
                    task.exception?.let { continuation.resumeWithException(it) }
                }
            }
        }
    }

    override suspend fun getDefaultUrl() {
        suspendCoroutine<Uri?> { continuation ->
            imagesRef.child("icon 105x150.png").downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    continuation.resume(downloadUri)
                } else {
                    task.exception?.let { continuation.resumeWithException(it) }
                }
            }
        }
    }
}
