package com.example.facial_feedback_app.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy

fun Bitmap.toGrayscale(): IntArray {

    val RED2GS = 0.299
    val GREEN2GS = 0.587
    val BLUE2GS = 0.114

    var pixel:Int
    val pixels = IntArray(this.width*this.height)

    (0 until this.height).forEach {y->

        (0 until this.width).forEach { x->

            pixel= this.getPixel(x,y)
//            val color =Color(pixel)

            val redPart = android.graphics.Color.red(pixel)
            val greenPart = android.graphics.Color.green(pixel)
            val bluePart = android.graphics.Color.blue(pixel)

            pixels[y*this.width+x]=((redPart * RED2GS + greenPart* GREEN2GS + bluePart *BLUE2GS).toInt())

        }
    }

    return pixels

}



fun Bitmap.resize(x:Int,y:Int): Bitmap {

    return Bitmap.createScaledBitmap(this, x, y, false)
}


fun ImageProxy.toRotatedBitmap():Bitmap{
    val matrix = Matrix().apply {
        postRotate(this@toRotatedBitmap.imageInfo.rotationDegrees.toFloat())
    }

    return Bitmap.createBitmap(
            this.toBitmap(),
            0,
            0,
            this.width,
            this.height,
            matrix,
            true
    )


}