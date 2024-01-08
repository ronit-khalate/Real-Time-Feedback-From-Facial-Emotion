package com.example.facial_feedback_app.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.util.Log
import android.util.Range
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.impl.VideoCaptureConfig
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facial_feedback_app.presentation.camera.state.CameraModeState
import com.example.facial_feedback_app.presentation.camera.state.RecordingState
import com.example.facial_feedback_app.utils.MlKitFaceDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@HiltViewModel
class CameraViewModel @Inject constructor(
        val mlKitFaceDetector: MlKitFaceDetector
):ViewModel() {


    val faceDetector = mlKitFaceDetector.mlKitFaceDetector
    // Which mode Camera or Video
    var cameraModeState:CameraModeState by mutableStateOf(CameraModeState.Camera())
        private set



    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    var bitmaps = _bitmaps.asStateFlow()

    var loading by mutableStateOf(false)

    fun addFaces(faces:List<Bitmap>){
        _bitmaps.value+=faces
    }

    var recording:Recording?=null


    fun takePhoto(
        controller: LifecycleCameraController,
       applicationContext: Context
    ){

        controller.takePicture(
                ContextCompat.getMainExecutor(applicationContext),
                object : ImageCapture.OnImageCapturedCallback(){
                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        Log.e("Camera", exception.message.toString())

                    }

                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        Log.e("CameraSuccess",image.toString())
                    }

                }
        )
    }


    // Starting video recording or capturing image
    fun onStart(controller: LifecycleCameraController,applicationContext: Context){

        when(cameraModeState){

            is CameraModeState.Video->{
                if(cameraModeState.recordingState is RecordingState.Started){
                    cameraModeState = CameraModeState.Video( RecordingState.Stopped)
                    recording?.close()
                    recording=null

                }
                else{
                    cameraModeState = CameraModeState.Video( RecordingState.Started)
                    recordVideo(controller, applicationContext)

                }
            }

            is CameraModeState.Camera->{

                takePhoto(controller =controller,applicationContext=applicationContext)
            }
        }
    }
    fun changeCameraMode(){

        cameraModeState = when(cameraModeState){

            is CameraModeState.Camera->{
                CameraModeState.Video(RecordingState.Stopped)
            }

            is CameraModeState.Video->{
                CameraModeState.Camera()
            }
        }
    }


    private fun recordVideo(controller: LifecycleCameraController,applicationContext: Context){

        /*
        *
        * */
       recording= controller.startRecording(
                FileOutputOptions.Builder(File(applicationContext.filesDir,"feedback.mp4")).build(),
                AudioConfig.AUDIO_DISABLED,
                ContextCompat.getMainExecutor(applicationContext)
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