package com.example.facial_feedback_app.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

fun Bitmap.toGrayscale(): Bitmap {
    val width = this.width
    val height = this.height

    val bitmapResult = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmapResult)
    val paint = Paint()

    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f) // Set saturation to 0 for grayscale

    val colorFilter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = colorFilter

    canvas.drawBitmap(this, 0f, 0f, paint)

    return bitmapResult
}

fun Bitmap.resize(x:Int,y:Int): Bitmap {

    return Bitmap.createScaledBitmap(this, x, y, false)
}