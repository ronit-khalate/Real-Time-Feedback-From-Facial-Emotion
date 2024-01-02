package com.example.facial_feedback_app.presentation.utils

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
import com.example.facial_feedback_app.presentation.BottomNavItem


@Composable

fun BottomNavigationBar(
    onCameraClick:()->Unit,
    onStorageClick:()->Unit

){

    var selected by remember{ mutableStateOf<BottomNavItem>(BottomNavItem.image) }

   NavigationBar(
           modifier = Modifier
               .height(50.dp)
   ) {
       NavigationBarItem(
               selected = selected is BottomNavItem.image,
               onClick = {
                   if(selected  !is BottomNavItem.image){

                       onCameraClick()
                       selected= BottomNavItem.image
                   }
              },
               icon = { Icon(imageVector = ImageVector.vectorResource(id = BottomNavItem.image.icon), contentDescription ="" ) }
       )

       NavigationBarItem(
               selected = selected is BottomNavItem.storage,
               onClick = {
                   onStorageClick()
                   selected =BottomNavItem.storage
                         },
               icon = { Icon(imageVector = ImageVector.vectorResource(id = BottomNavItem.storage.icon), contentDescription = "")})
   }
}
