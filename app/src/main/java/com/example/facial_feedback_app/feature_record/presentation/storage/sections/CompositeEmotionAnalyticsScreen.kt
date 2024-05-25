package com.example.facial_feedback_app.feature_record.presentation.storage.sections

import android.graphics.Typeface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.example.facial_feedback_app.feature_record.presentation.camera.CameraViewModel
import com.example.facial_feedback_app.feature_record.presentation.storage.components.CompositeAnalyticsDropDownMenuItem
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.fullWidth
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberFadingEdges
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlinx.coroutines.launch

@Composable

fun CompositeEmotionAnalyticsScreen(
    modifier: Modifier = Modifier
        .fillMaxSize(),
    viewModel:CameraViewModel
) {


    val scrollState = rememberScrollState()

    var isDropDownMenuExpanded by remember {
        mutableStateOf(false)
    }


    val scope = LocalLifecycleOwner.current.lifecycleScope







    Column(

            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxSize()
                .scrollable(
                        state = scrollState,
                        orientation = Orientation.Vertical
                )

    ) {

        Column(
                modifier= Modifier
                    .fillMaxWidth()


        ) {

            Card(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .height(30.dp)
                        .clickable {
                            isDropDownMenuExpanded = true
                        }
            ) {

               LazyRow(
                       modifier = Modifier
                           .padding(start = 20.dp, end = 20.dp)
                           .fillMaxSize(),
                       horizontalArrangement = Arrangement.SpaceEvenly,
                       verticalAlignment = Alignment.CenterVertically

               ) {

                   items(viewModel.addedEmotionsInCompositeAnalyticsState.keys.toList()){
                       Text(text = it.toString())
                   }

               }
            }

            Box(
                    modifier = Modifier
                        .fillMaxWidth()
            ){
                DropdownMenu(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        expanded =isDropDownMenuExpanded ,
                        onDismissRequest = { isDropDownMenuExpanded=false }
                ) {

                    Emotions.values().forEach { emotion: Emotions ->



                        CompositeAnalyticsDropDownMenuItem(
                                emotion = emotion.toString(),
                                isAdded = viewModel.addedEmotionsInCompositeAnalyticsState.keys.contains(emotion)
                        ) {

                                scope.launch {

                                    viewModel.addEmotionDataInCompositeChart(emotion)
                                }

                        }




                    }
                }
            }

        }


//
//        CartesianChartHost(
//                chart = rememberCartesianChart(
//
//                        rememberColumnCartesianLayer(
//
//
//                                ColumnCartesianLayer.ColumnProvider.series(
//
//                                        rememberLineComponent(
//                                                color = Color(0xffff5500),
//                                                thickness = 16.dp,
//                                                shape = remember { Shape.rounded(allPercent = 40) },
//                                        ),
//
//                                        ),
//
//                                ),
//                        startAxis = rememberStartAxis(
//
//                                titleComponent =
//                                rememberTextComponent(
//                                        color = Color.Black,
//                                        background = rememberShapeComponent(Shape.Pill, Color.Transparent),
//                                        padding = Dimensions.of(horizontal = 8.dp, vertical = 2.dp),
//                                        margins = Dimensions.of(end = 4.dp),
//                                        typeface = Typeface.MONOSPACE,
//                                ),
//                                title ="Probability",
//                        ),
//                        bottomAxis = rememberBottomAxis(
//
//                                itemPlacer = remember {
//                                    AxisItemPlacer.Horizontal.default(
//                                            spacing = 3,
//                                            addExtremeLabelPadding = true
//                                    )
//                                },
//                                titleComponent =
//                                rememberTextComponent(
//                                        color = Color.Black,
//                                        background = rememberShapeComponent(Shape.Pill, Color.Transparent),
//                                        padding = Dimensions.of(horizontal = 8.dp, vertical = 2.dp),
//                                        margins = Dimensions.of(end = 4.dp),
//                                        typeface = Typeface.MONOSPACE,
//                                ),
//                                title ="Time In Seconds",
//
//                                ),
//                ),
//
//                marker = rememberDefaultCartesianMarker(label = TextComponent.build()),
//                horizontalLayout = HorizontalLayout.fullWidth(),
//                model = viewModel.chartModelState.columnChartModel!!,
//                scrollState = rememberVicoScrollState(),
//                zoomState = rememberVicoZoomState()
//        )

        Spacer(modifier = Modifier.height(30.dp))

        CartesianChartHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                chart =
                rememberCartesianChart(


                        legend = rememberHorizontalLegend(
                                items = viewModel.compositeAnalyticsChartLegends,
                                iconSize = 10.dp,
                                iconPadding = 20.dp,
                                spacing = 10.dp
                        ),
                        layers = arrayOf(
                                rememberLineCartesianLayer(
                                        lines = viewModel.compositeAnalyticsChartLineSpecs.map {

                                            remember {
                                                it
                                            }
                                        }
                                )
                        ),
                        startAxis = rememberStartAxis(
                                guideline = null,
                                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                                titleComponent =
                                rememberTextComponent(
                                        color = Color.Black,
                                        background = rememberShapeComponent(Shape.Pill, Color.Transparent),
                                        padding = Dimensions.of(horizontal = 8.dp, vertical = 2.dp),
                                        margins = Dimensions.of(end = 4.dp),
                                        typeface = Typeface.MONOSPACE,
                                ),
                                title ="Probability",
                        ),
                        bottomAxis =
                        rememberBottomAxis(
                                titleComponent =
                                rememberTextComponent(
                                        color = Color.Black,
                                        background = rememberShapeComponent(Shape.Pill, Color.Transparent),
                                        padding = Dimensions.of(horizontal = 8.dp, vertical = 2.dp),
                                        margins = Dimensions.of(end = 4.dp),
                                        typeface = Typeface.MONOSPACE,
                                ),
                                title ="Time In Seconds",
                        ),
                        fadingEdges = rememberFadingEdges(),
                ),
                modelProducer = viewModel.compositeAnalyticsModel,
                horizontalLayout = HorizontalLayout.fullWidth(),
                scrollState = rememberVicoScrollState(),
                zoomState = rememberVicoZoomState(zoomEnabled = false),
        )
    }
    
}