package com.android.mycalcinstapplicationtumanov.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns.IS_PENDING
import android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.mycalcinstapplicationtumanov.R
import com.android.mycalcinstapplicationtumanov.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), ChooseSourceDialog.OnFileSelectedListener {

    private var _binding : FragmentHomeBinding? = null
    private var mUri: Uri? = null
    val TAG = "HomeFragment"
    var currentPhotoPath: String = ""

    private val binding get() = _binding!!
    private var isReadPermissionGranted = false
    private var isWritePermissionGranted = false
    private val REQUEST_IMAGE_CAPTURE = 1001
    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String>
    var profilePhotoBitmap : Bitmap? = null


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

    private fun sdkCheck() : Boolean{

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return true
        }

        return false

    }

    private fun requestPermission(){

        val isReadPermission = ContextCompat.checkSelfPermission(
            requireContext().applicationContext,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val isWritePermission = ContextCompat.checkSelfPermission(
            requireContext().applicationContext,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSdkLevel = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        isReadPermissionGranted = isReadPermission
        isWritePermissionGranted = isWritePermission || minSdkLevel

        val permissionRequest = mutableListOf<String>()
        if (!isWritePermissionGranted){

            permissionRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }
        if (!isReadPermissionGranted){

            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        }

        if (permissionRequest.isNotEmpty())
        {
            requestPermissionLauncher.launch(permissionRequest.toTypedArray().toString())
        }

    }

    private fun savePhotoToExternalStorage(name : String, bmp : Bitmap?) : Boolean{
        var contentResolver = requireActivity().contentResolver

        val imageCollection : Uri = if (sdkCheck()){
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }else{
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {

            put(MediaStore.Images.Media.DISPLAY_NAME,"$name.jpg")
            put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
            if (bmp != null){
                put(MediaStore.Images.Media.WIDTH,bmp.width)
                put(MediaStore.Images.Media.HEIGHT,bmp.height)
            }

        }

        return try{

            contentResolver.insert(imageCollection,contentValues)?.also {

                contentResolver.openOutputStream(it).use { outputStream ->

                    if (bmp != null){

                        if(!bmp.compress(Bitmap.CompressFormat.JPEG,95,outputStream)){

                            throw IOException("Failed to save Bitmap")
                        }
                    }
                }

            } ?: throw IOException("Failed to create Media Store entry")
            true
        }catch (e: IOException){

            e.printStackTrace()
            false
        }

    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.e(TAG, "ну всё начало работать")

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            Log.e(TAG, "ну всё сработало")
        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val packageManager = requireContext().packageManager
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    Log.e(TAG, "ну всё работает")
                    createImageFile()

                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e(TAG, "хуй")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = getUriForFile(
                        requireContext(),
                        "com.android.mycalcinstapplicationtumanov.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted : Boolean ->
                if (isGranted) {
                    //makePhoto()
                    //takePhoto.launch()
                    //dispatchTakePictureIntent()
                    capturePhoto()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Для изменения фото нужно разрешить доступ к камере",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun makePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e : ActivityNotFoundException) {
            // display error state to the user
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode : Int, resultCode : Int, intent : Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            intent?.extras?.let {
                val uri = it.get("uri") as Uri
                addImageUriToGallery(uri)
            }
        }
    }

    private fun capturePhoto(){
        val capturedImage = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CurrentAlbumArt.png")
        if(capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            getUriForFile(requireContext(), "com.android.mycalcinstapplicationtumanov.fileprovider",
                capturedImage)
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }


    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            requireContext().sendBroadcast(mediaScanIntent)
        }
    }


    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                selectedPhotoUri
            )
            else -> {
                val source = ImageDecoder.createSource(requireContext().contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return bitmap
    }

    fun addImageUriToGallery(uri : Uri){
        val resolver = requireContext().contentResolver
        val pictureCollection = MediaStore.Images.Media
            .getContentUri(VOLUME_EXTERNAL_PRIMARY)
        val pictureDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "CurrentAlbumArt.png")
            put(IS_PENDING, 1)
        }
        val b = getCapturedImage(uri)
        uri.let {
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
        uri.let { resolver.update(it, pictureDetails, null, null) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun addImageToGallery(b : Bitmap) {
        val resolver = requireContext().contentResolver
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

//        val filename = "file.png"
//        val file : FileOutputStream
//        try { file = FileOutputStream(Environment.getExternalStorageDirectory().toString() + "folder/" + filename)
//            Log.e("EROR",file.toString())
//            profilePhotoBitmap.compress(Bitmap.CompressFormat.PNG, 98, file)
//            file.flush()
//            file.close()
//            Log.e(null, "bip-bup" + file.toString())
//        } catch (e : Exception) {
//            e.printStackTrace()
//            Log.e(null, "Save file error!")
//        }
//        }
//    }

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



//class MainActivity : AppCompatActivity() {
//    //Our variables
//    private var mImageView: ImageView? = null
//    private var mUri: Uri? = null
//    //Our widgets
//    private lateinit var btnCapture: Button
//    private lateinit var btnChoose : Button
//    //Our constants
//    private val OPERATION_CAPTURE_PHOTO = 1
//    private val OPERATION_CHOOSE_PHOTO = 2
//
//
//    private fun initializeWidgets() {
//        btnCapture = findViewById(R.id.btnCapture)
//        btnChoose = findViewById(R.id.btnChoose)
//        mImageView = findViewById(R.id.mImageView)
//    }
//
//    private fun show(message: String) {
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
//    }
//    private fun capturePhoto(){
//        val capturedImage = File(externalCacheDir, "My_Captured_Photo.jpg")
//        if(capturedImage.exists()) {
//            capturedImage.delete()
//        }
//        capturedImage.createNewFile()
//        mUri = if(Build.VERSION.SDK_INT >= 24){
//            FileProvider.getUriForFile(this, "info.camposha.kimagepicker.fileprovider",
//                capturedImage)
//        } else {
//            Uri.fromFile(capturedImage)
//        }
//
//        val intent = Intent("android.media.action.IMAGE_CAPTURE")
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
//        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
//    }
//    private fun openGallery(){
//        val intent = Intent("android.intent.action.GET_CONTENT")
//        intent.type = "image/*"
//        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
//    }
//    private fun renderImage(imagePath: String?){
//        if (imagePath != null) {
//            val bitmap = BitmapFactory.decodeFile(imagePath)
//            mImageView?.setImageBitmap(bitmap)
//        }
//        else {
//            show("ImagePath is null")
//        }
//    }
//    private fun getImagePath(uri: Uri?, selection: String?): String {
//        var path: String? = null
//        val cursor = contentResolver.query(uri, null, selection, null, null )
//        if (cursor != null){
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
//            }
//            cursor.close()
//        }
//        return path!!
//    }
//    @TargetApi(19)
//    private fun handleImageOnKitkat(data: Intent?) {
//        var imagePath: String? = null
//        val uri = data!!.data
//        //DocumentsContract defines the contract between a documents provider and the platform.
//        if (DocumentsContract.isDocumentUri(this, uri)){
//            val docId = DocumentsContract.getDocumentId(uri)
//            if ("com.android.providers.media.documents" == uri.authority){
//                val id = docId.split(":")[1]
//                val selsetion = MediaStore.Images.Media._ID + "=" + id
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    selsetion)
//            }
//            else if ("com.android.providers.downloads.documents" == uri.authority){
//                val contentUri = ContentUris.withAppendedId(Uri.parse(
//                    "content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
//                imagePath = getImagePath(contentUri, null)
//            }
//        }
//        else if ("content".equals(uri.scheme, ignoreCase = true)){
//            imagePath = getImagePath(uri, null)
//        }
//        else if ("file".equals(uri.scheme, ignoreCase = true)){
//            imagePath = uri.path
//        }
//        renderImage(imagePath)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>
//                                            , grantedResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
//        when(requestCode){
//            1 ->
//                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
//                    PackageManager.PERMISSION_GRANTED){
//                    openGallery()
//                }else {
//                    show("Unfortunately You are Denied Permission to Perform this Operataion.")
//                }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode){
//            OPERATION_CAPTURE_PHOTO ->
//                if (resultCode == Activity.RESULT_OK) {
//                    val bitmap = BitmapFactory.decodeStream(
//                        getContentResolver().openInputStream(mUri))
//                    mImageView!!.setImageBitmap(bitmap)
//                }
//            OPERATION_CHOOSE_PHOTO ->
//                if (resultCode == Activity.RESULT_OK) {
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        handleImageOnKitkat(data)
//                    }
//                }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        initializeWidgets()
//
//        btnCapture.setOnClickListener{capturePhoto()}
//        btnChoose.setOnClickListener{
//            //check permission at runtime
//            val checkSelfPermission = ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
//                //Requests permissions to be granted to this application at runtime
//                ActivityCompat.requestPermissions(this,
//                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
//            }
//            else{
//                openGallery()
//            }
//        }
//    }
//}