package com.salim.mypokedex.profile

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import com.bumptech.glide.Glide
import com.salim.mypokedex.R
import com.salim.mypokedex.databinding.FragmentProfileBinding
import com.salim.mypokedex.utilities.EspressoCounterIdlingResource
import com.salim.mypokedex.utilities.GlideIdlingResourceTarget
import com.salim.mypokedex.utilities.TakePictureWithUriReturnContract
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var currentPhotoPath: String

    private val viewModel: ProfileViewModel by viewModels()

    private val cameraIntent = registerForActivityResult(TakePictureWithUriReturnContract()) {
        if(it.isSuccess) {
            Glide.with(this).load(it.result).into(binding.avatarView)
            viewModel.submitNewAvatar(it.result.toString())
        }
    }

    private val galleryIntent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Glide.with(this).load(uri).into(binding.avatarView)
            viewModel.submitNewAvatar(uri.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view)
        EspressoCounterIdlingResource.increment()

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        with(binding) {
            updateAvatarButton.setOnClickListener {
                viewModel.triggerShowAvatarOptionsDialogEvent()
            }

            saveProfileButton.setOnClickListener {
                viewModel.submitNewProfileInfo(
                    name = nameEditText.text.toString(),
                    email = emailEditText.text.toString()
                )
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            profileFlow.asLiveData().observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.emailEditText.setText(it.email)
                    binding.nameEditText.setText(it.name)

                    if(it.avatarImageLocation.isNotEmpty()) {
                        val uri = Uri.parse(it.avatarImageLocation)
                        Glide.with(this@ProfileFragment).load(uri).into(
                            GlideIdlingResourceTarget(
                                EspressoCounterIdlingResource,
                                binding.avatarView
                            )
                        )
                        EspressoCounterIdlingResource.decrement()
                    } else {
                        EspressoCounterIdlingResource.decrement()
                    }
                }
            }

            showAvatarDialogEvent.asLiveData().observe(viewLifecycleOwner) {
                if(it) {
                    showAvatarDialog()
                }
            }

            profileUpdatedEventFlow.asLiveData().observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), R.string.profile_updated, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showAvatarDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.avatar)
            .setMessage(R.string.avatar_description)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton(R.string.gallery) { dialog, _ ->
                showGallery()
                dialog.dismiss()
            }
            .setPositiveButton(R.string.camera) { dialog, _ ->
                showCameraV2()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showGallery() {
        galleryIntent.launch(IMAGE_TYPE)
    }

    private fun showCameraV2() {
        //val uri = Uri.fromFile(createImageFile())
        EspressoCounterIdlingResource.increment()
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            createImageFile()
        )
        cameraIntent.launch(uri)
    }

    private fun showCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1234
        const val IMAGE_TYPE = "image/*"
    }
}