package com.KopohGames.calculator.ui.gallery


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.KopohGames.calculator.databinding.FragmentGalleryBinding
import java.io.File


class GalleryFragment : Fragment() {

    private var _binding : FragmentGalleryBinding? = null
    val TAG = "GalleryFragment"
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root : View = binding.root

        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContacts.apply {
            adapter = ListAdapter(context, getAllShownImagesPath(requireActivity()))
            layoutManager = LinearLayoutManager(context)
        }
    }

    @SuppressLint("Recycle")
    private fun getAllShownImagesPath(activity : Activity) : ArrayList<Uri> {
        val cursor : Cursor?
        val listOfAllImages = ArrayList<Uri>()
        var absolutePathOfImage : String
        val uri : Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        cursor = activity.contentResolver.query(
            uri, projection, null,
            null, null
        )

        val column_index_data : Int = cursor!!.getColumnIndexOrThrow(MediaColumns.DATA)
        cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            val cringe = Uri.fromFile(File(absolutePathOfImage))
            listOfAllImages.add(cringe)
        }
        listOfAllImages.reverse()
        return listOfAllImages
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

