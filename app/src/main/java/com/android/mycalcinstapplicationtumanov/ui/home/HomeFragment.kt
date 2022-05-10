package com.android.mycalcinstapplicationtumanov.ui.home

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media.IS_PENDING
import android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.mycalcinstapplicationtumanov.databinding.FragmentHomeBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), ChooseSourceDialog.OnFileSelectedListener {

    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val TAG = "HomeFragment"

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private val REQUEST_IMAGE_CAPTURE = 1001
    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String>
    var profilePhotoBitmap : Bitmap? = null

    var vFilename : String = ""

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root : View = binding.root

        binding.igGaaag.setOnClickListener {
            showChooseDialog()
        }

        return root
    }


    private fun showChooseDialog() {
        ChooseSourceDialog.instance(this).show(parentFragmentManager, "choose")
    }


    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted : Boolean ->
                    if (isGranted) {
                        openCamera()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Для изменения фото нужно разрешить доступ к камере",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Log.e(TAG, "Sorry you're version android is not support, Min Android 6.0 (Marsmallow)")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode : Int,
        permissions : Array<out String>,
        grantResults : IntArray
    ) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    openCamera()
                }
            }
        }
    }

//
//    private fun makePhoto() {
//
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        } catch (e : ActivityNotFoundException) {
//            // display error state to the user
//        }
//    }

    @SuppressLint("SimpleDateFormat", "SdCardPath")
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")

        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // set filename
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        vFilename = "FOTO_$timeStamp.jpg"

        // set direcory folder
        val file = File("/sdcard/AppDir/", vFilename);
        val image_uri = FileProvider.getUriForFile(
            requireContext().applicationContext,
            requireContext().applicationContext.applicationContext.packageName + ".provider",
            file
        )

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun addImageToGallery(b : Bitmap) {
        val resolver = requireContext().applicationContext.contentResolver

        val pictureCollection = MediaStore.Images.Media
            .getContentUri(VOLUME_EXTERNAL_PRIMARY)

        val pictureDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "CurrentAlbumArt.png")
            put(IS_PENDING, 1)
        }

        val pictureContentUri = resolver.insert(pictureCollection, pictureDetails)

        pictureContentUri?.let {
            resolver.openFileDescriptor(it, "w", null).use { pfd ->
                try {
                    pfd?.let {
                        val fos = FileOutputStream(it.fileDescriptor)
                        b.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        fos.close()
                    }
                } catch (e : IOException) {
                    e.printStackTrace()
                }
            }
        }

        pictureDetails.clear()
        pictureDetails.put(IS_PENDING, 0)
        pictureContentUri?.let { resolver.update(it, pictureDetails, null, null) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            //File object of camera image
            val file = File("/sdcard/niabsen/", vFilename);
            Log.e(TAG, file.toString())

            //Uri of camera image
            val uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".provider",
                file
            )
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
            val Bitmap = ImageDecoder.decodeBitmap(source)

            //val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            addImageToGallery(Bitmap)
            //myImageView.setImageURI(uri)
        }
    }
    /*override fun onActivityResult(requestCode : Int, resultCode : Int, intent : Intent?) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
        intent?.extras?.let {
            profilePhotoBitmap = it.get("data") as Bitmap
            //saveProfilePhoto(this.profilePhotoBitmap!!)
            addImageToGallery(profilePhotoBitmap!!)
//                val bitmap = uriToBitmap(image_uri!!)
//                frame?.setImageBitmap(bitmap)

        }
    }
}*/


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFileSelectListener(select : Int) {
        if (select == ChooseSourceDialog.selectPhoto) {
            requestPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }
}


/*override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(imageBitmap)
            addImageToGallery(imageBitmap)
        }
    }*/

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
//
//            // if multiple images are selected
//            if (data?.getClipData() != null) {
//                val count = data.clipData!!.itemCount
//
//                for (i in 0..count - 1) {
//                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
//
//                    val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
//                    addImageToGallery(bitmap)
//                //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
//                }
//
//            } else if (data?.getData() != null) {
//                // if single image is selected
//                val imageUri: Uri? = data.data
//                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
//                addImageToGallery(bitmap)
//                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview
//
//            }
//        }
//    }


//    private fun openCamera() {
//
//        //ask camera and read storage permission
//        val permissionRequests = mutableListOf<String>()
//        permissionRequests.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//
//        //ask write external storage permission when sdk is less than 28
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            permissionRequests.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
//
//        Dexter.withContext(context?.applicationContext)
//            .withPermissions(
//                permissionRequests
//            ).withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
//
//                    if (report.areAllPermissionsGranted()) {
//                        Log.d(TAG, "onPermissionsChecked: all granted")
//
//                        val values = ContentValues()
//                        values.put(MediaStore.Images.Media.TITLE, "New Picture")
//                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
//                        currentImageUri = contentResolver.insert(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
//                        )
//                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
//                        resultLauncher.launch(intent)
//
//
//                    } else {
//                        Log.d(TAG, "onPermissionsChecked: not granted")
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: List<PermissionRequest>,
//                    token: PermissionToken
//                ) { /* ... */
//                }
//            }).check()
//
//
//    }