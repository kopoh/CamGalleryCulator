package com.android.mycalcinstapplicationtumanov.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import com.android.mycalcinstapplicationtumanov.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseSourceDialog : BottomSheetDialogFragment(), View.OnClickListener {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = View.inflate(context, R.layout.dialog_choose_source, null)
        if (view != null) {
            dialog.setContentView(view)
            view.findViewById<View>(R.id.btnCamera).setOnClickListener(this)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (R.id.btnCamera === id) {
            getResults(selectPhoto)
        } else
        dismiss()
    }

    private fun getResults(select: Int) {
        listener.onFileSelectListener(select)
        dismiss()
    }

    interface OnFileSelectedListener {
        fun onFileSelectListener(select: Int)
    }

    companion object {
        const val selectPhoto = 2
        private lateinit var listener: OnFileSelectedListener

        fun instance(listener : HomeFragment): ChooseSourceDialog {
            Companion.listener = listener
            return ChooseSourceDialog()
        }
    }
}
