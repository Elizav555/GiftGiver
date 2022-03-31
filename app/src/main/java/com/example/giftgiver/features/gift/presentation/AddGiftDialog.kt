package com.example.giftgiver.features.gift.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.giftgiver.App
import com.example.giftgiver.BuildConfig
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ImageViewModel
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.DialogAddGiftBinding
import java.io.File
import javax.inject.Inject

class AddGiftDialog(private val submitAction: (String, String, File?) -> Unit?) : DialogFragment() {
    private lateinit var binding: DialogAddGiftBinding
    private var cameraImageFile: File? = null
    private var imageFile: File? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var imageViewModel: ImageViewModel
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            imageViewModel.onGalleryImagePicked(it)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            cameraImageFile?.let { file ->
                imageViewModel.onCameraImagePicked(file)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        App.mainComponent.inject(this)
        imageViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[ImageViewModel::class.java]
        binding = DialogAddGiftBinding.inflate(layoutInflater)
        binding.btnCamera.setOnClickListener { openCamera() }
        binding.btnGallery.setOnClickListener { openGallery() }
        imageViewModel.imageBitmapLiveData.observe(this) {
            binding.ivImage.setImageBitmap(it)
        }
        imageViewModel.imageFileLiveData.observe(this) {
            Log.d("ImageFile", it?.absolutePath.toString())
            imageFile = it
        }
        with(binding) {
            return activity?.let {
                val dialog = AlertDialog.Builder(it, R.style.MyDialogTheme).setView(root)
                    .setPositiveButton(getString(R.string.save)) { _, _ ->
                        submitAction(etName.text.toString(), etDesc.text.toString(), imageFile)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->
                        dialog?.cancel()
                    }.create()
                etName.addTextChangedListener {
                    checkInputs(dialog)
                }
                etDesc.addTextChangedListener {
                    checkInputs(dialog)
                }
                dialog.setOnShowListener {
                    checkInputs(dialog)
                }
                dialog
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    private fun checkInputs(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = with(binding) {
            etName.text.toString().isNotBlank() && etDesc.text.toString().isNotBlank()
        }
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
