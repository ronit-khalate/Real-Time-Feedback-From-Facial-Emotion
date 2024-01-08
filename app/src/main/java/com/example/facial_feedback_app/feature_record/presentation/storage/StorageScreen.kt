package com.example.facial_feedback_app.feature_record.presentation.storage

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun StorageReview(
    cameraViewModel: CameraViewModel
){

    val bitmaplist = cameraViewModel.bitmaps.collectAsState().value
    LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
    ){
        items(
                items = bitmaplist
        ){ bitmap ->
            
            Image(
                    bitmap = bitmap.asImageBitmap(),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription =""
            )
        }
    }
}