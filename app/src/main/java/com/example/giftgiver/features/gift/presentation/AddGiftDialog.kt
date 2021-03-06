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
import androidx.fragment.app.viewModels
import coil.load
import com.example.giftgiver.BuildConfig
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.DialogAddGiftBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.images.presentation.ImageViewModel
import dagger.android.support.DaggerDialogFragment
import java.io.File
import javax.inject.Inject

class AddGiftDialog(
    private val submitAction: (String, String, File?) -> Unit?,
    private val gift: Gift? = null
) : DaggerDialogFragment() {
    private lateinit var binding: DialogAddGiftBinding
    private var cameraImageFile: File? = null
    private var imageFile: File? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val imageViewModel: ImageViewModel by viewModels { viewModelFactory }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
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
        binding = DialogAddGiftBinding.inflate(layoutInflater)
        with(binding) {
            btnCamera.setOnClickListener { openCamera() }
            btnGallery.setOnClickListener { openGallery() }
            imageViewModel.imageBitmapLiveData.observe(this@AddGiftDialog) {
                ivImage.setImageBitmap(it)
            }
            imageViewModel.imageFileLiveData.observe(this@AddGiftDialog) {
                Log.d("ImageFile", it?.absolutePath.toString())
                imageFile = it
            }
            gift?.let {
                etName.setText(it.name)
                etDesc.setText(it.desc)
                ivImage.load(it.imageUrl){
                    crossfade(true)
                    placeholder(R.drawable.default_gift)
                }
            }
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
