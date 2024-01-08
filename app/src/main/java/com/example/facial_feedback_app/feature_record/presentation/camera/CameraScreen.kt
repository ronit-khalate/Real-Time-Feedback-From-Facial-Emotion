package com.example.facial_feedback_app.feature_record.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.Range
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.ImageAnalysisConfig
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.example.facial_feedback_app.R
import com.example.facial_feedback_app.feature_record.presentation.MainViewModel
import com.example.facial_feedback_app.feature_record.presentation.camera.state.CameraModeState
import com.example.facial_feedback_app.feature_record.presentation.camera.state.RecordingState
import com.example.facial_feedback_app.feature_record.presentation.utils.CameraPreview
import com.example.facial_feedback_app.utils.toRotatedBitmap
import com.google.mlkit.vision.face.Face
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    viewmodel: CameraViewModel,
    applicationContext: Context,
){

    val context= LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current
    val cameraController = remember{


        LifecycleCameraController(context).apply {
            // setting frame rated
            videoCaptureTargetFrameRate= Range(5,10)
            setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE or
                    CameraController.VIDEO_CAPTURE or
                    CameraController.IMAGE_ANALYSIS
            )


            bindToLifecycle(lifecycleOwner)


        }
    }

    cameraController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context)){imageProxy:ImageProxy->

        Log.d("recording",cameraController.isRecording.toString())
        if(viewmodel.cameraModeState.recordingState is RecordingState.Started && cameraController.isRecording){

            viewmodel.mlKitFaceDetector.getFacesFromCapturedImage(imageProxy.toRotatedBitmap()){faceBitmapList:List<Bitmap>,faceList:List<Face>->


                viewmodel.addFaces(faceBitmapList)



            }
        }
        imageProxy.close()

    }



    Box(
            modifier = Modifier
                .fillMaxSize()
    ){

        CameraPreview(controller =cameraController,Modifier.fillMaxSize() )

        
        
        Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
        ) {

            Surface(
                    modifier = Modifier
                        .height(80.dp)
                        .width(270.dp),
                    shape = RoundedCornerShape(70.dp),
                    color = Color.White.copy(alpha = 0.6f)
            ) {

                Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    // Timer
//                   AnimatedVisibility(
//                           visibleState = MutableTransitionState(initialState = viewmodel.cameraModeState.recordingState is RecordingState.Started)
//                   ) {
//
//                       Surface(
//                               modifier = Modifier
//                                   .height(30.dp)
//                                   .width(30.dp),
//                               shape = RoundedCornerShape(40.dp)
//                       ) {
//                           Text(text = viewmodel.timer.toString())
//                       }
//                   }


                    Surface(
                            modifier = Modifier
                                .height(60.dp)
                                .width(60.dp),
                            shape = CircleShape
                    ){}


                    // Button to start or pause recording
                    Column(
                        modifier = Modifier
                            .height(70.dp)
                            .width(70.dp)
                            .border(width = 3.dp, color = Color.White, shape = CircleShape),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {



                        Surface(
                                modifier = viewmodel.cameraModeState.recordingState.modifier
                                    .clickable {

                                        viewmodel.onStart(
                                                controller = cameraController,
                                                context = context
                                        )

                                    },
                                shape = viewmodel.cameraModeState.recordingState.shape,
                                color = viewmodel.cameraModeState.recordingState.color
                        ){}

                    }

                    Surface(
                          modifier = Modifier
                              .height(57.dp)
                              .width(57.dp)
                              .border(width = 3.dp, color = Color.White, shape = CircleShape)
                              .blur(60.dp),
                          shape = CircleShape,
                          color = Color.White.copy(alpha = 0.2f)
                    ){

                       IconButton(onClick = {
                          viewmodel.changeCameraMode()
                       }) {
                           Icon(painter = painterResource(id = R.drawable.tune), contentDescription ="Change")
                       }
                    }


                }

            }


        }

        if(viewmodel.loading){
            CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.surfaceVariant,

            )
        }


    }





}

@Preview
@Composable
fun preview(){
//    CameraScreen( applicationContext = LocalContext.current)
}

