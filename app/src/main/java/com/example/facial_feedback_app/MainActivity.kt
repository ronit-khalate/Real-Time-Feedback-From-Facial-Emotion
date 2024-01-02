package com.example.facial_feedback_app

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.facial_feedback_app.presentation.BottomNavItem
import com.example.facial_feedback_app.presentation.MainViewModel
import com.example.facial_feedback_app.presentation.utils.BottomNavigationBar
import com.example.facial_feedback_app.presentation.camera.CameraScreen
import com.example.facial_feedback_app.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.presentation.storage.StorageReview
import com.example.facial_feedback_app.presentation.utils.PermissionManager
import com.example.facial_feedback_app.ui.theme.FacialFeedbackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        if(!PermissionManager.hasPermission(applicationContext)){

            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(PermissionManager.CAMAERAX_PERMISSIONS),
                    0
            )
        }

        setContent {
            FacialFeedbackTheme {
                val cameraViewModel:CameraViewModel= hiltViewModel()
                val navController = rememberNavController()



                Scaffold(

                    bottomBar = {
                        BottomNavigationBar(
                                onCameraClick = {
                                    navController.navigate(BottomNavItem.image.screen_route)
                                },
                                onStorageClick = {
                                    navController.navigate(BottomNavItem.storage.screen_route)
                                }
                        )
                    }
                ) {

                    Column(
                            modifier = Modifier
                                .padding(it)
                    ) {

                        NavHost(navController = navController, startDestination = BottomNavItem.image.screen_route){

                            composable(route = BottomNavItem.image.screen_route){
                                CameraScreen(
                                    applicationContext = applicationContext,
                                    viewmodel = cameraViewModel
                                )
                            }

                            composable(route = BottomNavItem.storage.screen_route){
                                StorageReview(cameraViewModel=cameraViewModel)
                            }




                        }

                    }


                }
            }
        }



    }


}


