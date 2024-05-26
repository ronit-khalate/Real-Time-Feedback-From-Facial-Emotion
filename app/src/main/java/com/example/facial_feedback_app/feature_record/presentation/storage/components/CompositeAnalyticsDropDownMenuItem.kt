package com.example.facial_feedback_app.feature_record.presentation.storage.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CompositeAnalyticsDropDownMenuItem(
    modifier: Modifier = Modifier,
    emotion:String,
    isAdded:Boolean=false,
    onClick:(Boolean)->Unit
) {


    Row(

            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable {
                    onClick(isAdded)
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = emotion)

        if(isAdded) {


            Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
            )
        }
    }

}