package com.example.giftgiver.common.db.fileStorage

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.StorageReference
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ImageStorageImpl(
    private val storageRef: StorageReference,
) : ImageStorage {
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

    override suspend fun getDefaultUrl() =
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

    override suspend fun deleteImage(url: String) {
        if (url == getDefaultUrl().toString())
            return
        imagesRef.storage.getReferenceFromUrl(url).delete().addOnCompleteListener {
            if (it.isSuccessful) {
            } else {
                Log.e("deleteImage", it.toString())
            }
        }
    }
}
