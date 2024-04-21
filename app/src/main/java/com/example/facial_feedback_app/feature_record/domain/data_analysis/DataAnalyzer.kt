package com.example.facial_feedback_app.feature_record.domain.data_analysis

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.feature_record.presentation.camera.state.RecordingState
import com.example.facial_feedback_app.utils.MlKitFaceDetector
import com.example.facial_feedback_app.utils.toRotatedBitmap
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class DataAnalyzer @Inject constructor(
       private val mlKitFaceDetector: MlKitFaceDetector
) {


    var overAllEmotions:MutableList<Long> = mutableListOf()
        private set

    // Time series Emotion collection
    /**
     * List<FloatArray> is used because each frame may have multiple faces
     * to each face has one float array for its emotions
     * */
    val timeSeriesEmotionCollection:MutableMap<Long,List<FloatArray>> = mutableMapOf()



    /**
    * This method gives time series of particular emotions
    * Need Emotions enums object
    * */
    fun getEmotionTimeSeriesData(emotion: Emotions): Map<Long, FloatArray> {



        return timeSeriesEmotionCollection.map {
            it.key to it.value[emotion.ordinal]
        }.toMap()

    }


    fun updateData(time:Long,emotionMap:List<FloatArray>){
        timeSeriesEmotionCollection[time] = emotionMap


    }

    fun analyze(time:Long,emotionMap:List<Long>){


    }


    fun imageProxyFlow(cameraController: LifecycleCameraController,context:Context,viewModel: CameraViewModel):Flow<Bitmap> = callbackFlow{



        cameraController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context)){imageProxy: ImageProxy ->

            Log.d("recording", "hi")

            imageProxy.use {
                if(viewModel.cameraModeState.recordingState is RecordingState.Started && cameraController.isRecording){

                    trySend(imageProxy.toRotatedBitmap())
                }
            }






//            imageProxy.use {
//                if(viewModel.cameraModeState.recordingState is RecordingState.Started && cameraController.isRecording){
//
//                    viewModel.mlKitFaceDetector.getFacesFromCapturedImage(it.toRotatedBitmap()){emotionMapOfFrame:Map<Int,Float>->
//
//
//
//                        viewModel.updateEmotionSumMap(emotionMapOfFrame)
//
//
//
//                    }
//                }
//            }



        }

        awaitClose {

        }

    }
}