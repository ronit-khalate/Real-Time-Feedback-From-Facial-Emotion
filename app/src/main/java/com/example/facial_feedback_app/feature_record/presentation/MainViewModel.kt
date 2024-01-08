package com.example.facial_feedback_app.feature_record.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor():ViewModel() {

//    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
//    var bitmaps = _bitmaps.asStateFlow()
//
//    var loading by mutableStateOf(false)
//
//    fun onTakePhoto(bitmap:Bitmap){
//
//
//            getFaces(bitmap)
//
//
//    }
//
//    fun Bitmap.toGrayscale(): Bitmap {
//        val width = this.width
//        val height = this.height
//
//        val bitmapResult = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmapResult)
//        val paint = Paint()
//
//        val colorMatrix = ColorMatrix()
//        colorMatrix.setSaturation(0f) // Set saturation to 0 for grayscale
//
//        val colorFilter = ColorMatrixColorFilter(colorMatrix)
//        paint.colorFilter = colorFilter
//
//        canvas.drawBitmap(this, 0f, 0f, paint)
//
//        return bitmapResult
//    }
//
//    fun Bitmap.resize(x:Int,y:Int):Bitmap{
//
//        return Bitmap.createScaledBitmap(this,x,y,false)
//    }
//
//    // Face Detection model parameter
//    private val faceDetectorOptions = FaceDetectorOptions.Builder()
//        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
//        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
//        .build()
//
//    // Face Detector
//    private val faceDetector = FaceDetection.getClient(faceDetectorOptions)
//
//    fun getFaces(bitmap:Bitmap){
//
//        val image =InputImage.fromBitmap(bitmap,0)
//        var facesList:MutableList<Bitmap> = mutableListOf()
//        faceDetector.process(image)
//            .addOnSuccessListener { faces->
//
//                Log.d("Success","${faces.size}")
//                faces.forEach {
//                    val boundingBox = it.boundingBox
//                    var face =Bitmap.createBitmap(
//                            bitmap,
//                            boundingBox.left,
//                            boundingBox.top,
//                            boundingBox.width(),
//                            boundingBox.height()
//                    )
//
//
//                    //TODO: Use resize function before passing images to model
//                    facesList.add(face.toGrayscale().resize(48,48))
//                }
//
//                _bitmaps.value+=facesList
//
//
//
//
//
//
//            }
//            .addOnFailureListener{
//                Log.e("Detect",it.message.toString())
//            }
//
//        }


    }

