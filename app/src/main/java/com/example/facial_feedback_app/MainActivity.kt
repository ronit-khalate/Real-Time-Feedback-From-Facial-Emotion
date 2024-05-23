package com.example.facial_feedback_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.facial_feedback_app.core.permissions.PermissionManager
import com.example.facial_feedback_app.feature_record.presentation.BottomNavItem
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraScreen
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.feature_record.presentation.storage.AnalyticsScreen
import com.example.facial_feedback_app.feature_record.presentation.utils.BottomNavigationBar
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
                val cameraViewModel: CameraViewModel = hiltViewModel()
                val navController = rememberNavController()



                Scaffold(

                    bottomBar = {
                        BottomNavigationBar(
                                onCameraClick = {
                                    navController.navigate(BottomNavItem.Camera.screen_route)
                                },
                                onStorageClick = {
                                    navController.navigate(BottomNavItem.Analytics.screen_route)
                                }
                        )
                    }
                ) {

                    Column(
                            modifier = Modifier
                                .padding(it)
                    ) {

                        NavHost(navController = navController, startDestination = BottomNavItem.Camera.screen_route){

                            composable(route = BottomNavItem.Camera.screen_route){
                                CameraScreen(
                                        viewmodel = cameraViewModel
                                )
                            }

                            composable(route = BottomNavItem.Analytics.screen_route){
                                AnalyticsScreen(cameraViewModel=cameraViewModel)
                            }




                        }

                    }


                }
            }
        }



    }


}


