package com.example.giftgiver.features.client.presentation

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
import coil.api.load
import com.example.giftgiver.BuildConfig
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ImageViewModel
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.DialogEditClientBinding
import com.example.giftgiver.features.client.domain.Client
import dagger.android.support.DaggerDialogFragment
import java.io.File
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EditClientDialog(private val client: Client) : DaggerDialogFragment() {
    private val dateRegex =
        Regex("^(1[0-9]|0[1-9]|3[0-1]|2[1-9]|[1-9]).(0[1-9]|1[0-2]|[1-9]).[0-9]{4}$")
    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val todayDate = Calendar.getInstance()
    private lateinit var binding: DialogEditClientBinding
    private var cameraImageFile: File? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val imageViewModel: ImageViewModel by viewModels { viewModelFactory }
    private var imageFile: File? = null
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
        binding = DialogEditClientBinding.inflate(layoutInflater)
        binding.btnCamera.setOnClickListener { openCamera() }
        binding.btnGallery.setOnClickListener { openGallery() }
        binding.etName.setText(client.info.name)
        binding.etInfo.setText(client.info.about)
        binding.etBirth.setText(client.info.bdate)
        binding.ivImage.load(client.info.photo)
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
                        (parentFragment as? AccountFragment)?.updateInfo(
                            etName.text.toString(),
                            etInfo.text.toString(),
                            etBirth.text.toString(),
                            imageFile
                        )
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->
                        dialog?.cancel()
                    }.create()
                etName.addTextChangedListener {
                    checkInputs(dialog)
                }
                etInfo.addTextChangedListener {
                    checkInputs(dialog)
                }
                etBirth.addTextChangedListener {
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
            etName.text.toString().isNotBlank() && etInfo.text.toString()
                .isNotBlank() && validateBirthDate(etBirth.text.toString())
        }
    }

    private fun validateBirthDate(date: String): Boolean {
        if (!date.matches(dateRegex)) {
            return false
        }
        return try {
            val birthDate = dateFormat.parse(date)
            if (birthDate.after(todayDate.time)) {
                false
            } else {
                val birthCalendar = Calendar.getInstance()
                birthCalendar.time = birthDate
                todayDate.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR) <= 100
            }
        } catch (ex: ParseException) {
            false
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
