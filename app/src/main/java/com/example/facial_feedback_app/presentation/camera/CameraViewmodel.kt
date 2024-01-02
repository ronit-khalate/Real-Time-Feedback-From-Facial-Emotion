package com.example.facial_feedback_app.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
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

@HiltViewModel
class CameraViewModel @Inject constructor(
        private val mlKitFaceDetector: MlKitFaceDetector
):ViewModel() {


    val faceDetector = mlKitFaceDetector.mlKitFaceDetector
    // Which mode Camera or Video
    var cameraModeState:CameraModeState by mutableStateOf(CameraModeState.Camera())
        private set



    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    var bitmaps = _bitmaps.asStateFlow()

    var loading by mutableStateOf(false)


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
                        Log.e("cap",image.toString())
                        val matrix = Matrix().apply {
                            postRotate(image.imageInfo.rotationDegrees.toFloat())

                        }
                        val rotatedBitmap =Bitmap.createBitmap(
                                image.toBitmap(),
                                0,
                                0,
                                image.width,
                                image.height,
                                matrix,
                                true
                        )
                        //* Passing Captured Image To FaceDetector


                        mlKitFaceDetector.getFacesFromCapturedImage(rotatedBitmap){faceList->
                            
                            _bitmaps.value+=faceList
                        }

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

        viewModelScope.launch(Dispatchers.Default){

            while (cameraModeState.recordingState is RecordingState.Started){

                takePhoto(controller=controller,applicationContext=applicationContext)
                delay(500L)
            }
        }
    }

}