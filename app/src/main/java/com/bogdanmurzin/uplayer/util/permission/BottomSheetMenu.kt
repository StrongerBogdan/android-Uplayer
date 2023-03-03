package com.bogdanmurzin.uplayer.util.permission

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import com.bogdanmurzin.uplayer.R

class BottomSheetMenu(context: Context, listener: () -> Unit) {

    private val bottomSheetDialog: Dialog = Dialog(context)
    var view: View = LayoutInflater.from(context).inflate(R.layout.layout_bottomsheet, null)

    init {
        bottomSheetDialog.setContentView(view)

        val proceedButton: Button = view.findViewById(R.id.button_proceed)
        proceedButton.setOnClickListener { listener.invoke() }
    }

    fun setDescription(@StringRes descRes: Int) {
        view.findViewById<TextView>(R.id.description).setText(descRes)
    }

    fun show() {
        bottomSheetDialog.show()
    }

    fun hide() {
        bottomSheetDialog.hide()
    }

    interface CustomListener {
        fun onProceedToAskPermission()
    }
}