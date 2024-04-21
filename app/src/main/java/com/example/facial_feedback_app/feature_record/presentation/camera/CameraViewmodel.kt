package com.example.facial_feedback_app.feature_record.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facial_feedback_app.feature_record.domain.Camera
import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.example.facial_feedback_app.feature_record.domain.data_analysis.DataAnalyzer
import com.example.facial_feedback_app.feature_record.presentation.camera.state.CameraModeState
import com.example.facial_feedback_app.feature_record.presentation.camera.state.RecordingState
import com.example.facial_feedback_app.utils.MlKitFaceDetector
import com.google.mlkit.vision.face.Face
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
        val mlKitFaceDetector: MlKitFaceDetector,
        val camera: Camera,
        val dataAnalyzer: DataAnalyzer,
):ViewModel() {


    var startTimeOfRecording:Long = 0L


    lateinit var model:CartesianChartModel



    // Which mode Camera or Video
    var cameraModeState: CameraModeState by mutableStateOf(CameraModeState.Camera())
        private set

    private var emotionSumMap:MutableMap<Int,Float> = mutableMapOf()




    private val _faceList = MutableStateFlow<List<Face>>(emptyList())

    var faceList = _faceList

    var loading by mutableStateOf(false)



    // Starting video recording or capturing image
    fun onStart(controller: LifecycleCameraController,context: Context){

        when(cameraModeState){

            is CameraModeState.Video->{
                if(cameraModeState.recordingState is RecordingState.Started){
                    cameraModeState = CameraModeState.Video(RecordingState.Stopped)
                   camera.closeRecoding()

                    viewModelScope.launch {

                        analyze()
                    }.invokeOnCompletion {

                    }

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

        startTimeOfRecording=System.currentTimeMillis()
        camera.recordVideo(controller=controller, context = context)
    }


    fun updateEmotionSumMap(emotionMap:Map<Int,Float>){

       emotionMap.forEach { (key,value)->

           emotionSumMap[key]= value + emotionSumMap.getOrDefault(key,0.0F);

       }
    }

    fun analyzeFrame(bitmap: Bitmap){

        Log.d("analyzeFrame",bitmap.toString())

        mlKitFaceDetector.getFacesFromCapturedImage(bitmap){

            dataAnalyzer.updateData(System.currentTimeMillis()-startTimeOfRecording,it)
        }
    }

    suspend fun analyze(){

        var _x = mutableListOf<Long>()
        var _y= mutableListOf<Float>()

        val happy = dataAnalyzer.getEmotionTimeSeriesData(Emotions.HAPPY)

        val happyTimeSeries=dataAnalyzer.getEmotionTimeSeriesData(Emotions.HAPPY)

        val aveage = happyTimeSeries.map {

            it.key to it.value
        }.toMap()

        model= CartesianChartModel(

                ColumnCartesianLayerModel.build {

                    series(x = aveage.keys, y =(1..100).toList())
                }
        )


    }


}