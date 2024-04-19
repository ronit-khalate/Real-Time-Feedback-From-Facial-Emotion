package com.example.facial_feedback_app.feature_record.domain.data_analysis

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.feature_record.presentation.camera.state.RecordingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class DataAnalyzer @Inject constructor() {


    var overAllEmotions:MutableList<Long> = mutableListOf()
        private set

    // Time series Emotion collection
    val timeSeriesEmotionCollection:MutableMap<Long,List<Long>> = mutableMapOf()



    /**
    * This method gives time series of particular emotions
    * Need Emotions enums object
    * */
    fun getEmotionTimeSeriesData(emotion: Emotions): Map<Long, Long> {



        return timeSeriesEmotionCollection.map {
            it.key to it.value[emotion.ordinal]
        }.toMap()

    }


    fun updateData(allEmotionMap:List<Float>){
        
    }


    fun imageProxyFlow(cameraController: LifecycleCameraController,context:Context,viewModel: CameraViewModel):Flow<ImageProxy> = callbackFlow{

        cameraController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context)){imageProxy: ImageProxy ->

            Log.d("recording", cameraController.isRecording.toString())

            if(viewModel.cameraModeState.recordingState is RecordingState.Started && cameraController.isRecording){

                trySend(imageProxy)
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

    }
}