package com.example.facial_feedback_app.feature_record.presentation.utils

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(
    controller:LifecycleCameraController,
    modifier:Modifier=Modifier,
    addRectView:(Int,Int)->Unit
){
    val lifecycleOwner = LocalLifecycleOwner.current


    AndroidView(
            factory = { context ->

                PreviewView(context).apply {
                    this.controller=controller

                    controller.bindToLifecycle(lifecycleOwner)





                }

            },


            modifier=modifier



            )
}