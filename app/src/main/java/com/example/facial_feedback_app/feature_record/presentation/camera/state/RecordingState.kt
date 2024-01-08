package com.example.facial_feedback_app.feature_record.presentation.camera.state


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

sealed class RecordingState(
        val modifier: Modifier,
        val shape: Shape,
        val color:Color
) {

    object Stopped: RecordingState(
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
                .padding(6.dp),
            shape = CircleShape,
            color = Color.Red.copy(alpha = 0.7f)
    )

    object Started: RecordingState(
            modifier = Modifier
                .height(45.dp)
                .width(45.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(8.dp)),
            shape = RectangleShape,
            color = Color.Red.copy(alpha = 0.7f)
    )

    object clicked: RecordingState(
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
                .padding(6.dp),
            shape = CircleShape,
            color = Color.White
    )
}