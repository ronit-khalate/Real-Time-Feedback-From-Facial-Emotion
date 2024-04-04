package com.example.facial_feedback_app.feature_record.presentation.camera

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.facial_feedback_app.R
import com.example.facial_feedback_app.feature_record.domain.StorageImage
import com.example.facial_feedback_app.feature_record.presentation.camera.state.RecordingState
import com.example.facial_feedback_app.feature_record.presentation.utils.CameraPreview
import com.example.facial_feedback_app.utils.toRotatedBitmap
import com.google.mlkit.vision.face.Face


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    viewmodel: CameraViewModel,
) {

    val configuration = LocalConfiguration.current
    var screenHeight  by remember {
        mutableStateOf(0F)
    }
    var screenWidth by remember {
        mutableStateOf(0F)
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var latestFaceBound  = viewmodel.faceList.collectAsState(initial = emptyList()).value



    val cameraController = remember{


        LifecycleCameraController(context).apply {
            // setting frame rated

            imageAnalysisOutputImageFormat=ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE or
                    CameraController.VIDEO_CAPTURE or
                    CameraController.IMAGE_ANALYSIS
            )

//            setImageAnalysisAnalyzer(
//                    ContextCompat.getMainExecutor(context),
//                    MlKitAnalyzer(
//                            listOf(viewmodel.mlKitFaceDetector.mlKitFaceDetector),
//                            ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL,
//                            ContextCompat.getMainExecutor(context)
//                    ) { result: MlKitAnalyzer.Result? ->
//
//                        result?.getValue(viewmodel.mlKitFaceDetector.mlKitFaceDetector).let {faceList->
//                            if (viewmodel.cameraModeState.recordingState is RecordingState.Started && this.isRecording) {
//
//                                faceList?.let {
//                                    viewmodel.updateFaceListFlow(it)
//                                } ?: viewmodel.updateFaceListFlow(emptyList())
//                            }
//                        }
//
//
//                    }.apply {
//                        defaultTargetResolution
//                    }
//            )




            bindToLifecycle(lifecycleOwner)


        }
    }


    cameraController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context)){imageProxy: ImageProxy ->
        
        Log.d("recording", cameraController.isRecording.toString())

        if(viewmodel.cameraModeState.recordingState is RecordingState.Started && cameraController.isRecording){

            viewmodel.mlKitFaceDetector.getFacesFromCapturedImage(imageProxy.toRotatedBitmap()){faceBitmapList:List<StorageImage>,_faceList:List<Face>->


                viewmodel.addFaces(faceBitmapList)



            }
        }
        imageProxy.close()

    }




    Box(
            modifier = Modifier
                .fillMaxSize()

    ){

        CameraPreview(controller =cameraController,Modifier.matchParentSize()){w,h->
            screenWidth=w.toFloat()
            screenHeight=h.toFloat()
        }

        Text(text = "preW=$screenWidth , preH=$screenHeight", fontSize = 20.sp, color = Color.Red)



        Canvas(
                modifier = Modifier
                    .matchParentSize()
        ) {

            val width = this.size.width * this.density
            val height = this.size.height * this.density


            drawRect(
                    color = Color.Transparent,
                    size = Size(size.width, size.height)
            )

              latestFaceBound.forEach { face ->

//                  val xScale = screenWidth / face.boundingBox.width()
//                  val yScale = screenHeight/ face.boundingBox.height()
//
//                  val adjustedLeft = face.boundingBox.left * xScale
//                  val adjustedTop = face.boundingBox.top * yScale
//                  val adjustedRight = face.boundingBox.right * xScale
//                  val adjustedBottom = face.boundingBox.bottom * yScale
//
//                  val _rect =android.graphics.Rect(
//                          adjustedLeft.toInt(),adjustedTop.toInt(),adjustedRight.toInt(),adjustedBottom.toInt()
//                  )
//
////                      face.boundingBox.toComposeRect().copy(
////                              left = adjustedLeft,
////                              top = adjustedTop,
////                              right = adjustedRight,
////                              bottom = adjustedBottom
////                      )
//
//                   val zero =0
                  val composeRect = face.boundingBox.toComposeRect()
//                  val rect = face.boundingBox.toComposeRect().copy(
//                          left = if(composeRect.left<0) zero.toFloat() else composeRect.left,
//                          top = if (composeRect.top<0)  zero.toFloat() else composeRect.top,
//                          bottom = if(composeRect.bottom>screenHeight) screenHeight else composeRect.bottom,
//                          right = if(composeRect.right>screenWidth) screenWidth else composeRect.right
//
//                  )

                  drawRect(
                          color = Color.Red,
                          topLeft = composeRect.topLeft,
                          size=composeRect.size,
                          style = Stroke(5f)
                  )
              }



        }

        
        
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

