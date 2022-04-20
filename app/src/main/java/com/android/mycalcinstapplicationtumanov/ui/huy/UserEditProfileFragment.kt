package com.android.mycalcinstapplicationtumanov.ui.huy

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.android.mycalcinstapplicationtumanov.databinding.UserEditProfileFragmentBinding
import com.android.mycalcinstapplicationtumanov.ui.home.ChooseSourceDialog


class UserEditProfileFragment : Fragment(), ChooseSourceDialog.OnFileSelectedListener {

    companion object {
        fun newInstance() = UserEditProfileFragment()
    }

    private lateinit var binding: UserEditProfileFragmentBinding
    private val USER_PHOTO = "user_photo"
    private val USER_PHONE = "user_phone"
    private val USER_FULL_NAME = "user_full_name"
    private val USER_EMAIL = "user_email"
    val REQUEST_IMAGE_CAPTURE = 1001
    val REQUEST_GALLERY = 1002
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var profilePhotoUrl: String? = null
    var profilePhotoBitmap: Bitmap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserEditProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    makePhoto()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Для изменения фото нужно разрешить доступ к камере",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        binding.apply {

            ivProfile.setOnClickListener {
                showChooseDialog()
            }
        }
    }

    override fun onFileSelectListener(select: Int) {
        if (select == ChooseSourceDialog.selectFile) {
            openFile()
        } else if (select == ChooseSourceDialog.selectPhoto) {
            requestPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    private fun showChooseDialog() {
        //ChooseSourceDialog.instance(this).show(parentFragmentManager, "choose")
    }

    private fun makePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            intent?.extras?.let {
                profilePhotoBitmap = it.get("data") as Bitmap
                //saveProfilePhoto(profilePhotoBitmap!!)
            }
        } else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            intent?.data?.let {
                profilePhotoBitmap = getBitmap(it)
               // saveProfilePhoto(profilePhotoBitmap!!)
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Ошибка загрузки фото",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun openFile() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }

    fun getBitmap(imageUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        val contentResolver: ContentResolver = requireContext().contentResolver
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            } else {
                val source = ImageDecoder.createSource(contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }



}


