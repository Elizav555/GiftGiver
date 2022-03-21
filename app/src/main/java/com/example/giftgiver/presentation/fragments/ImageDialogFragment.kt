package com.example.giftgiver.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.giftgiver.BuildConfig
import com.example.giftgiver.R
import com.example.giftgiver.databinding.DialogImageBinding
import com.example.giftgiver.presentation.ImageViewModel
import java.io.File

open class ImageDialogFragment : DialogFragment() {
    private var cameraImageFile: File? = null
    private lateinit var binding: DialogImageBinding
    private val viewModel by viewModels<ImageViewModel>()

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            viewModel.onGalleryImagePicked(it)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            cameraImageFile?.let { file ->
                viewModel.onCameraImagePicked(file)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        binding = DialogImageBinding.inflate(layoutInflater)
        binding.btnCamera.setOnClickListener { openCamera() }
        binding.btnGallery.setOnClickListener { openGallery() }
        viewModel.imageBitmapLiveData.observe(this) {
            binding.ivImage.setImageBitmap(it)
        }
        viewModel.imageFileLiveData.observe(this) {
            Log.d("ImageFile", it?.absolutePath.toString())
        }

        return activity?.let {
            val dialog = AlertDialog.Builder(it).setView(binding.root)
                .setPositiveButton("Save changes") { _, _ ->
                    //todo save new image
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }.create()
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun openCamera() {
        val dir =
            ContextCompat.getExternalFilesDirs(requireContext(), Environment.DIRECTORY_PICTURES)[0]
        cameraImageFile = File.createTempFile("tempImg", "", dir).also {
            it.deleteOnExit()
        }
        cameraImageFile?.let {
            val imageUri = FileProvider.getUriForFile(
                requireContext(),
                "${BuildConfig.APPLICATION_ID}.fileprovider",
                it
            )
            cameraLauncher.launch(imageUri)
        }
    }
}
