package com.example.facial_feedback_app.feature_record.presentation.utils

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.facial_feedback_app.feature_record.presentation.BottomNavItem


@Composable

fun BottomNavigationBar(
    onCameraClick:()->Unit,
    onStorageClick:()->Unit

){

    var selected by remember{ mutableStateOf<BottomNavItem>(BottomNavItem.Camera) }

   NavigationBar(
           modifier = Modifier
               .height(50.dp)
   ) {
       NavigationBarItem(
               selected = selected is BottomNavItem.Camera,
               onClick = {
                   if(selected  !is BottomNavItem.Camera){

                       onCameraClick()
                       selected= BottomNavItem.Camera
                   }
              },
               icon = { Icon(imageVector = ImageVector.vectorResource(id = BottomNavItem.Camera.icon), contentDescription ="" ) }
       )

       NavigationBarItem(
               selected = selected is BottomNavItem.Analytics,
               onClick = {
                   onStorageClick()
                   selected = BottomNavItem.Analytics
                         },
               icon = { Icon(imageVector = ImageVector.vectorResource(id = BottomNavItem.Analytics.icon), contentDescription = "")})
   }
}
