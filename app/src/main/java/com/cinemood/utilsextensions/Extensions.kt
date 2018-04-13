package com.cinemood.utilsextensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.Fragment
import android.util.Log.d
import android.widget.ImageView
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Gson object parser extension
 */
inline fun <reified T> Gson.fromJson(json: String) =
        this.fromJson<T>(json, object: TypeToken<T>() {}.type)!!
