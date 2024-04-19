package com.example.facial_feedback_app.utils

import android.graphics.Bitmap
import android.util.Log
import com.example.facial_feedback_app.feature_record.domain.FaceBoundInfo
import com.example.facial_feedback_app.feature_record.domain.StorageImage
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
        addFaceToList:(emotionMap:Map<Int,Float>)->Unit
    ){

        val image = InputImage.fromBitmap(bitmap, 0)
        val faceBitmapList:MutableList<StorageImage> = mutableListOf()
        val facesList:MutableList<Face> = mutableListOf()
        val emotionMapOfFrame:MutableMap<Int,Float> = mutableMapOf()
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
                    val faceBound = FaceBoundInfo(
                            x=x,
                            y=y,
                            width=width,
                            height=height,
                            boundingBox=boundingBox
                    )

//                     Create a new Bitmap with the corrected dimensions
                    if (width > 0 && height > 0) {
                        val croppedFace = Bitmap.createBitmap(bitmap, x, y, width, height)

                        val emotionMap =emotionClassifier.classify(croppedFace,false)

                        emotionMapOfFrame.forEach { (key,value)->

                           emotionMapOfFrame[key]= emotionMapOfFrame.getOrDefault(key,0.0F) + value
                        }

                        val storageImage = StorageImage(croppedFace, emotionMap)
                        faceBitmapList.add(storageImage)
                        facesList.add(face)

                    }

                }

                addFaceToList(emotionMapOfFrame)
                emotionMapOfFrame.clear()

                faceBitmapList.clear()
                facesList.clear()


            }
            .addOnFailureListener{
                Log.e("Detect", it.message.toString())
            }

    }
}