package com.example.facial_feedback_app.feature_record.domain.data_analysis

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.example.facial_feedback_app.feature_record.domain.BitmapTimeStampWrapper
import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.feature_record.presentation.camera.state.RecordingState
import com.example.facial_feedback_app.utils.MlKitFaceDetector
import com.example.facial_feedback_app.utils.toRotatedBitmap
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class DataAnalyzer @Inject constructor(
       private val mlKitFaceDetector: MlKitFaceDetector
) {


    var overAllEmotions:MutableList<Long> = mutableListOf()
        private set

    var frameCount =0;



    // Time series Emotion collection
    /**
     * List<FloatArray> is used because each frame may have multiple faces
     * to each face has one float array for its emotions
     * */
    val timeSeriesEmotionCollection:MutableMap<Long,EmotionProbabilitiesPerFrame> = mutableMapOf()



    /**
    * This method gives time series of particular emotions
    * Need Emotions enums object
    * */
    fun getEmotionTimeSeriesData(emotion: Emotions): Map<Long, List<Float>> {

        return timeSeriesEmotionCollection.map {

            it.key to it.value.getEmotionProbabilityList(emotion)
        }.toMap()


    }




    fun updateData(time:Long,emotionMap:List<FloatArray>){

        val emotionsProbaPerFrame = EmotionProbabilitiesPerFrame().apply {

            addEmotionProbabilitiesOfFace(emotionMap)
        }

        Log.i("InfoEmotion","$time -> ${emotionsProbaPerFrame.getEmotionProbabilityList(Emotions.HAPPY).toString()}")



        if(!timeSeriesEmotionCollection.containsKey(time)){
            timeSeriesEmotionCollection[time] = emotionsProbaPerFrame
        }



    }

    fun analyze(time:Long,emotionMap:List<Long>){


    }


    fun imageProxyFlow(cameraController: LifecycleCameraController,context:Context,viewModel: CameraViewModel):Flow<BitmapTimeStampWrapper> = callbackFlow{



        cameraController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context)){imageProxy: ImageProxy ->

            frameCount++

            imageProxy.use {

                if (frameCount%10==0) {

                    frameCount=0
                    if (viewModel.cameraModeState.recordingState is RecordingState.Started && cameraController.isRecording) {


                        trySend(
                                BitmapTimeStampWrapper(
                                        it.toRotatedBitmap(),
                                        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                                )
                        )
                    }
                }

            }


        }

        awaitClose {

        }

    }
}