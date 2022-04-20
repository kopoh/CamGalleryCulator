package com.android.mycalcinstapplicationtumanov.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Bundle

class CalculatorViewModel : ViewModel() {

    val primary = MutableLiveData<Any>().apply {
        value = ""
    }
    val secndary = MutableLiveData<Any>().apply {
        value = 0.0
    }

    val textPrimary : LiveData<Any> = primary
    val textSeconday : LiveData<Any> = secndary

}
