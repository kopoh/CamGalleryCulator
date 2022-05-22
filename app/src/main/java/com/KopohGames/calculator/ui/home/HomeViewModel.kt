package com.KopohGames.calculator.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _mutableLiveDataBitMap = MutableLiveData<Any>().apply {

    }
    val BitmaliveData : LiveData<Any> = _mutableLiveDataBitMap
}