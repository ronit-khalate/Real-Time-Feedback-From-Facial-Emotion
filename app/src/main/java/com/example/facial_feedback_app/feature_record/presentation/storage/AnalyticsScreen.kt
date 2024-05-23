package com.example.facial_feedback_app.feature_record.presentation.storage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.facial_feedback_app.R
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.feature_record.presentation.storage.sections.SingleEmotionAnalyticsScreen


@Composable
fun AnalyticsScreen(
    cameraViewModel: CameraViewModel
){


    val scrollState = rememberScrollState()

    val navController = rememberNavController()

    Row(

            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = { /*TODO*/ }) {

                Image(
                        painter = painterResource(id = R.drawable.single) ,
                        contentDescription =null
                )
        }

        IconButton(onClick = { /*TODO*/ }) {
            Image(
                    painter = painterResource(id = R.drawable.combined) ,
                    contentDescription =null
            )
        }
    }

    Column(

            modifier = Modifier
                .fillMaxSize()

    ) {

        NavHost(
                navController = navController,
                startDestination = "Single"
        ){
            composable(route = "Single"){
                SingleEmotionAnalyticsScreen(viewModel = cameraViewModel)
            }

            composable(route="Combined"){

            }
        }






    }

}