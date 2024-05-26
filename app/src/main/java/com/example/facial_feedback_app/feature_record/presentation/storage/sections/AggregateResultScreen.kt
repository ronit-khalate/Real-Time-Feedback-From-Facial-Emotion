package com.example.facial_feedback_app.feature_record.presentation.storage.sections

import android.graphics.Typeface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.feature_record.presentation.storage.components.AggregateEmotionSelectorRow


@Composable
fun AggregateResultScreen(
    viewModel: CameraViewModel
) {


    Column(

            modifier = Modifier
                .padding(30.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {


            AggregateEmotionSelectorRow(viewModel)





        if(viewModel.pieChartDataState.slices.isNotEmpty()){


            PieChart(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(300.dp),
                    pieChartData = viewModel.pieChartDataState,
                    pieChartConfig = PieChartConfig(
                            isAnimationEnable = true,
                            animationDuration = 1500,
                            showSliceLabels = true,
                            sliceLabelTypeface = Typeface.MONOSPACE,
                            sumUnit = "%",


                            )
            )


            Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
            ) {


                Text(text = "Total Points : ${viewModel.totalDataPoints}")
                Text(text = "Total Positive Points: ${viewModel.totalPositiveDataPoints}")
                Text(text = "Total Negative Points: ${viewModel.totalNegativeDataPoints}")
                Text(text = "Total Data Points Considered: ${viewModel.totalDataPointsToConsider}")
                Text(text = "Total Data Points Not Considered: ${viewModel.totalDataPoints - viewModel.totalDataPointsToConsider}")
            }



        }


    }

}