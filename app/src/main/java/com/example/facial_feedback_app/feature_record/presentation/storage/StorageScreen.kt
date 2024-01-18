package com.example.facial_feedback_app.feature_record.presentation.storage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel


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

            Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                        bitmap = bitmap.asImageBitmap(),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp)),
                        contentDescription =""
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text("")
                Spacer(modifier = Modifier.height(5.dp))
                Text("")
                Spacer(modifier = Modifier.height(5.dp))
                Text("")
                Spacer(modifier = Modifier.height(5.dp))
            }

        }
    }
}