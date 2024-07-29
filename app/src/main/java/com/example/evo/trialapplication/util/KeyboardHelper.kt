package com.example.evo.trialapplication.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {


    fun hideKeyboard(view:View,context:Context) {

        val inputMethodManager=context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
    }


}