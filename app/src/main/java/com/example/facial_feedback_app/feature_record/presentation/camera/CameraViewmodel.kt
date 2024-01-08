package com.example.facial_feedback_app.feature_record.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.facial_feedback_app.feature_record.domain.Camera
import com.example.facial_feedback_app.feature_record.presentation.camera.state.CameraModeState
import com.example.facial_feedback_app.feature_record.presentation.camera.state.RecordingState
import com.example.facial_feedback_app.utils.MlKitFaceDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
        val mlKitFaceDetector: MlKitFaceDetector,
        val camera: Camera
):ViewModel() {



    // Which mode Camera or Video
    var cameraModeState: CameraModeState by mutableStateOf(CameraModeState.Camera())
        private set



    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    var bitmaps = _bitmaps.asStateFlow()

    var loading by mutableStateOf(false)

    fun addFaces(faces:List<Bitmap>){
        _bitmaps.value+=faces
    }







    // Starting video recording or capturing image
    fun onStart(controller: LifecycleCameraController,context: Context){

        when(cameraModeState){

            is CameraModeState.Video->{
                if(cameraModeState.recordingState is RecordingState.Started){
                    cameraModeState = CameraModeState.Video(RecordingState.Stopped)
                   camera.closeRecoding()

                }
                else{
                    cameraModeState = CameraModeState.Video(RecordingState.Started)
                    recordVideo(controller, context)

                }
            }

            is CameraModeState.Camera->{

                takePhoto(controller=controller,context=context)
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

    private fun takePhoto(
        controller: LifecycleCameraController,
        context: Context
    ){

        camera.takePhoto(controller,context)
    }
    private fun recordVideo(
        controller: LifecycleCameraController,
        context: Context
    ){

        camera.recordVideo(controller=controller, context = context)
    }

}