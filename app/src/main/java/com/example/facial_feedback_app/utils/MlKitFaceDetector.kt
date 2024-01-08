package com.example.facial_feedback_app.utils

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import javax.inject.Inject

class MlKitFaceDetector @Inject constructor() {


    // Face Detection model parameter
    private val faceDetectorOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .setMinFaceSize(100F)
        .build()

    // Face Detector
    val mlKitFaceDetector = FaceDetection.getClient(faceDetectorOptions)

    fun getFacesFromCapturedImage(
        bitmap: Bitmap,
        addFaceToList:(List<Bitmap>,List<Face>)->Unit
    ){

        val image = InputImage.fromBitmap(bitmap, 0)
        var facesList:MutableList<Bitmap> = mutableListOf()
        mlKitFaceDetector.process(image)
            .addOnSuccessListener { faces->
                Log.d("Success", "${faces.size}")
                faces.forEach {face->
                    val boundingBox = face.boundingBox


                    // Ensure that bounding box coordinates are within the bounds of the original bitmap
                    val x :Int= boundingBox.left.coerceAtLeast(0)
                    val y :Int= boundingBox.top.coerceAtLeast(0)
                    val width = boundingBox.width().coerceAtMost(bitmap.width - x)
                    val height = boundingBox.height().coerceAtMost(bitmap.height - y)

                    // Create a new Bitmap with the corrected dimensions
                    if (width > 0 && height > 0) {
                        val croppedFace = Bitmap.createBitmap(bitmap, x, y, width, height)
                        facesList.add(croppedFace.toGrayscale().resize(100,100))
                    }


                }

                addFaceToList(facesList,faces)
                facesList.clear()


            }
            .addOnFailureListener{
                Log.e("Detect", it.message.toString())
            }

    }
}