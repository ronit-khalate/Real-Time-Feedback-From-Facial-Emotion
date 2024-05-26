package com.example.facial_feedback_app.feature_record.presentation.storage.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.feature_record.presentation.storage.state.AggregateScreenEmotionSelectedState


@Composable
fun EmotionSmallCard(
    emotions: Emotions,
    viewModel: CameraViewModel,
    onClicked:(AggregateScreenEmotionSelectedState)->Unit
) {

    // else

    var selectedState by remember {

        mutableStateOf<AggregateScreenEmotionSelectedState>(viewModel.aggregateEmotionSelectedState[emotions]!!)

    }


    Card(

            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                    containerColor = when(selectedState){
                        is AggregateScreenEmotionSelectedState.SelectedAsDefault -> CardDefaults.cardColors().containerColor
                        is AggregateScreenEmotionSelectedState.SelectedAsNegative -> Color(0xfff68181)
                        is AggregateScreenEmotionSelectedState.SelectedAsPositive -> Color(0xff9bf09d)

                    }
            ),
            shape = RoundedCornerShape(20.dp),

            onClick = {
                
                  selectedState = when(selectedState){
                      AggregateScreenEmotionSelectedState.SelectedAsDefault ->AggregateScreenEmotionSelectedState.SelectedAsPositive
                      AggregateScreenEmotionSelectedState.SelectedAsPositive -> AggregateScreenEmotionSelectedState.SelectedAsNegative
                      AggregateScreenEmotionSelectedState.SelectedAsNegative -> AggregateScreenEmotionSelectedState.SelectedAsDefault
                  }

                onClicked(selectedState)
            }

    ) {

        Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp,top=7.dp, bottom = 7.dp),
                text = emotions.toString(),
                fontSize = 18.sp
        )
    }

}


