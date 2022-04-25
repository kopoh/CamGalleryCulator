package com.android.mycalcinstapplicationtumanov.ui.home

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.mycalcinstapplicationtumanov.R
import com.android.mycalcinstapplicationtumanov.databinding.FragmentHomeBinding
import java.io.File
import java.io.FileOutputStream


class HomeFragment : Fragment(), ChooseSourceDialog.OnFileSelectedListener {

    private var _binding : FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    val REQUEST_IMAGE_CAPTURE = 1001
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
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.userEditProfileFragment)
        }

        binding.igGaaag.setOnClickListener {
            showChooseDialog()
        }
        val textView : TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun showChooseDialog() {
        ChooseSourceDialog.instance(this).show(parentFragmentManager, "choose")
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted : Boolean ->
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
    }

    private fun makePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e : ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, intent : Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            intent?.extras?.let {
                profilePhotoBitmap = it.get("data") as Bitmap
                saveProfilePhoto(this.profilePhotoBitmap!!)
            }
        }
    }




    private fun saveProfilePhoto(profilePhotoBitmap : Bitmap) {

        profilePhotoBitmap.let {
            val dir =
                File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"picture.jpg")
            if (dir.exists()) dir.delete()
            val file = FileOutputStream(dir)
            dir.createNewFile()
            try{
                it.compress(
                    Bitmap.CompressFormat.JPEG,
                    98,
                    file
                )
                file.flush()
                file.close()
            }
            catch (e : Exception) {
                e.printStackTrace()
                Log.e(null, "Save file error!")
            }

            //Alert gallery to update images
            Log.e("EROR", file.toString())
        }
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