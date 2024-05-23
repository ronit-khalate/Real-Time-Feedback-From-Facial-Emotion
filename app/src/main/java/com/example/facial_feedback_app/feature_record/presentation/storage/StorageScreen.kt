package com.example.facial_feedback_app.feature_record.presentation.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.fullWidth
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.Shape


@Composable
fun AnalyticsScreen(
    cameraViewModel: CameraViewModel
){

    CartesianChartHost(
            chart = rememberCartesianChart(

                rememberColumnCartesianLayer(


                    ColumnCartesianLayer.ColumnProvider.series(

                        rememberLineComponent(
                                color = Color(0xffff5500),
                                thickness = 16.dp,
                                shape = remember { Shape.rounded(allPercent = 40) },
                        ),

                    ),

                ),
                startAxis = rememberStartAxis(),
                bottomAxis =  rememberBottomAxis(

                        itemPlacer =  remember {
                            AxisItemPlacer.Horizontal.default(spacing = 3, addExtremeLabelPadding = true)
                        }
                ),


            ),

            marker = rememberDefaultCartesianMarker(label = TextComponent.build()),
            horizontalLayout = HorizontalLayout.fullWidth(),
            model =cameraViewModel.model,
            scrollState = rememberVicoScrollState(),
            zoomState = rememberVicoZoomState()
    )
}