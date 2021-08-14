package com.katyrin.utils.extensions

import android.app.Activity
import android.view.View
import android.widget.Toast

fun Activity.toast(message: String): Unit =
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

fun View.toast(message: String): Unit =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()