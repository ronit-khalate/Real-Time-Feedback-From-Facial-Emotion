package com.example.facial_feedback_app.utils

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import javax.inject.Inject

class MlKitFaceDetector @Inject constructor() {


    // Face Detection model parameter
    private val faceDetectorOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .build()

    // Face Detector
    val mlKitFaceDetector = FaceDetection.getClient(faceDetectorOptions)

    fun getFacesFromCapturedImage(
        bitmap: Bitmap,
        addFaceToList:(List<Bitmap>)->Unit
    ){

        val image = InputImage.fromBitmap(bitmap, 0)
        var facesList:MutableList<Bitmap> = mutableListOf()
        mlKitFaceDetector.process(image)
            .addOnSuccessListener { faces->

                Log.d("Success", "${faces.size}")
                faces.forEach {
                    val boundingBox = it.boundingBox
                    var face = Bitmap.createBitmap(
                            bitmap,
                            boundingBox.left,
                            boundingBox.top,
                            boundingBox.width(),
                            boundingBox.height()
                    )


                    //TODO: Use resize function before passing images to model
                    facesList.add(face.toGrayscale())
                }

                addFaceToList(facesList)


            }
            .addOnFailureListener{
                Log.e("Detect", it.message.toString())
            }

    }
}