package com.example.evo.trialapplication.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.example.evo.trialapplication.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


fun Context.showToast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

@SuppressLint("RestrictedApi")
 fun customSnackBar(view: View,context: Context,message: String) {
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val snackBarView = inflater.inflate(R.layout.sweet_alert_layout, null)

    val snackBarText: TextView = snackBarView.findViewById(R.id.save)
    snackBarText.text =message
    val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
    snackBar.view.setBackgroundColor(Color.TRANSPARENT) // Set the background to transparent
    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout




    // Set the gravity of the SnackBarLayout
    val layoutParams = FrameLayout.LayoutParams(snackBarLayout.layoutParams)
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL


    snackBarLayout.layoutParams = layoutParams
    snackBarLayout.addView(snackBarView, 0) // Add the custom layout as the first child
    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBar.show()

}
