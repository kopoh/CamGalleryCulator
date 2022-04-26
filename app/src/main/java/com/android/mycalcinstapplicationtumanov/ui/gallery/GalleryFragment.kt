package com.android.mycalcinstapplicationtumanov.ui.gallery


import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mycalcinstapplicationtumanov.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {

    private var _binding : FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        val dashboardViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root : View = binding.root

        /*val textView : TextView = binding.
        dashboardViewModel.Bitmap.observe(viewLifecycleOwner) {
            textView.Bitmap = it
        }*/
        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContacts.apply {
            adapter = ListAdapter(context, createContacts())
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getLastImageFromGallery(i : Int) : Uri? {
        val uriExternal : Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.Media._ID,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )
        val cursor : Cursor = context?.contentResolver!!.query(
            uriExternal, projection, null,
            null, null
        )!!

        Log.i("Cursor Last", cursor.moveToLast().toString())
        if (cursor.moveToFirst()) {
            val columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val imageId : Long = cursor.getLong(columnIndexID) + i
            Log.i("Cursor Last", imageId.toString())
            val imageURI = Uri.withAppendedPath(uriExternal, "" + imageId)
            Log.i("Cursor Last!!!", imageId.toString())
            return imageURI
        }
        cursor.close()
        return null
    }

    fun getlast() : Int {
        val uriExternal : Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.Media._ID,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )
        val cursor : Cursor = context?.contentResolver!!.query(
            uriExternal, projection, null,
            null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
        )!!
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        return if (cursor.moveToFirst()) {
            // cursor.close();
            cursor.getString(column_index).toInt()
        } else -1
        // cursor.close();
    }

    private fun createContacts() : List<Uri> {
        val contacts = mutableListOf<Uri>()
        for (i in 0..5) getLastImageFromGallery(i)?.let { contacts.add(it) }
        return contacts
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

