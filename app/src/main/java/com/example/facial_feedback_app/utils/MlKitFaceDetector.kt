package com.example.facial_feedback_app.utils

import android.graphics.Bitmap
import android.util.Log
import com.example.facial_feedback_app.feature_record.domain.classifer.EmotionClassifierImpl
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MlKitFaceDetector @Inject constructor(
    private val emotionClassifier: EmotionClassifierImpl
) {



    // Face Detection model parameter
    private val faceDetectorOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .enableTracking()
        .build()

    // Object Detectiona and tracking

    private var _faceBoundFlow:MutableStateFlow<List<Face>> = MutableStateFlow(emptyList())



    // Face Detector
    private val mlKitFaceDetector = FaceDetection.getClient(faceDetectorOptions)

    fun getFacesFromCapturedImage(
        bitmap: Bitmap,
        onAllFacesGet:(List<FloatArray>)->Unit
    ){



        val image = InputImage.fromBitmap(bitmap, 0)

        val faceBitmapList:MutableList<Bitmap> = mutableListOf()
        val emotionMapOfFrame:MutableMap<Int,Float> = mutableMapOf()

        val emotionsOfAllFacesInFrame:MutableList<FloatArray> = mutableListOf()
        mlKitFaceDetector.process(image)
            .addOnSuccessListener { faces->

                if(faces.isNotEmpty())
                 _faceBoundFlow.update { faces }
                else
                    _faceBoundFlow.update { emptyList() }

                Log.d("Success", "${faces.size}")
                faces.forEach {face->
                    val boundingBox = face.boundingBox



                  // Ensure that bounding box coordinates are within the bounds of the original bitmap
                    val x :Int= boundingBox.left.coerceAtLeast(0)
                    val y :Int= boundingBox.top.coerceAtLeast(0)
                    val width = boundingBox.width().coerceAtMost(bitmap.width - x)
                    val height = boundingBox.height().coerceAtMost(bitmap.height - y)

//                     Create a new Bitmap with the corrected dimensions
                    if (width > 0 && height > 0) {
                        val croppedFace = Bitmap.createBitmap(bitmap, x, y, width, height)

                        var  faceEmotionArray =emotionClassifier.classify(croppedFace,false)
                        faceEmotionArray=faceEmotionArray.map {
                            it*100
                        }.toFloatArray()
                        emotionsOfAllFacesInFrame.add(faceEmotionArray)

                        emotionMapOfFrame.forEach { (key,value)->

                           emotionMapOfFrame[key]= emotionMapOfFrame.getOrDefault(key,0.0F) + value
                        }


                        faceBitmapList.add(croppedFace)


                    }

                }


                emotionMapOfFrame.clear()

                faceBitmapList.clear()
                onAllFacesGet(emotionsOfAllFacesInFrame)



            }
            .addOnFailureListener{
                Log.e("Detect", it.message.toString())
            }





    }
}