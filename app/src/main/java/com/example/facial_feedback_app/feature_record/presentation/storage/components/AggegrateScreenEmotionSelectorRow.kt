package com.example.facial_feedback_app.feature_record.presentation.storage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel


@Composable
fun AggregateEmotionSelectorRow(
    viewModel: CameraViewModel
) {

    Column(

            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {


        LazyRow(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(shape = RoundedCornerShape(20.dp)),

                ) {


            items(items = Emotions.values()) {emotion->


                Spacer(modifier = Modifier.width(10.dp))
                EmotionSmallCard(
                        emotions = emotion,
                        viewModel = viewModel
                ){
                    viewModel.onAggregateEmotionSelectedStateChanged(emotion,it)
                    viewModel.aggregateResultShow()
                }
            }


        }
        


    }
}

//@Preview(
//        showSystemUi = true
//)
//@Composable
//private fun EmotionSelectorRowPreview() {
//    AggregateEmotionSelectorRow()
//}