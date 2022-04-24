package com.android.mycalcinstapplicationtumanov.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text : LiveData<String> = _text


    lateinit var images: LiveData<List<String>>

    fun init(uid: String) {
        if (!this::images.isInitialized) {
            //images = usersRepo.getImages(uid)
        }
    }
}