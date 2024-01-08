package com.example.facial_feedback_app.feature_record.domain

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import java.io.File
import javax.inject.Inject

class Camera @Inject constructor() {


    var recording:Recording?=null
    fun takePhoto(
        controller: LifecycleCameraController,
        context: Context
    ){
        controller.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback(){
                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        Log.e("Camera", exception.message.toString())

                    }

                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        Log.e("CameraSuccess", image.toString())
                    }

                }
        )
    }

    fun closeRecoding(){
        recording?.close()
        recording=null
    }

    fun recordVideo(
        controller: LifecycleCameraController,
        context: Context
    ){
        recording= controller.startRecording(
                FileOutputOptions.Builder(File(context.filesDir, "feedback.mp4")).build(),
                AudioConfig.AUDIO_DISABLED,
                ContextCompat.getMainExecutor(context)
        ){videoRecordEvent->

            Log.d("record",videoRecordEvent.recordingStats.toString())
            when(videoRecordEvent){
                is VideoRecordEvent.Finalize->{
                    if(videoRecordEvent.hasError()){

                        recording?.close()
                        recording=null


                    }
                }


            }

        }
    }
}