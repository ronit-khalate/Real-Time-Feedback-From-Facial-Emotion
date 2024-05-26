package com.example.facial_feedback_app.feature_record.presentation.camera

import android.content.Context
import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.facial_feedback_app.feature_record.domain.BitmapTimeStampWrapper
import com.example.facial_feedback_app.feature_record.domain.Camera
import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.example.facial_feedback_app.feature_record.domain.data_analysis.DataAnalyzer
import com.example.facial_feedback_app.feature_record.presentation.camera.state.CameraModeState
import com.example.facial_feedback_app.feature_record.presentation.camera.state.RecordingState
import com.example.facial_feedback_app.feature_record.presentation.storage.state.AggregateScreenEmotionSelectedState
import com.example.facial_feedback_app.feature_record.presentation.storage.state.SingleEmotionAnalyticsCharModels
import com.example.facial_feedback_app.utils.MlKitFaceDetector
import com.google.mlkit.vision.face.Face
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
        val mlKitFaceDetector: MlKitFaceDetector,
        val camera: Camera,
        val dataAnalyzer: DataAnalyzer,
):ViewModel() {


    private var startTimeOfRecordingInSeconds:Long = 0L
        private set


    var chartModelState by mutableStateOf(SingleEmotionAnalyticsCharModels())
        private set



    // Which mode Camera or Video
    var cameraModeState: CameraModeState by mutableStateOf(CameraModeState.Camera())
        private set

    private var emotionSumMap:MutableMap<Int,Float> = mutableMapOf()




    private val _faceList = MutableStateFlow<List<Face>>(emptyList())

    var faceList = _faceList

    var loading by mutableStateOf(false)

    // Single Emotion Analytic Screen States
    var selectedEmotion  by mutableStateOf(Emotions.HAPPY)



    // Composite Screen State

    var compositeAnalyticsModel = CartesianChartModelProducer.build()
    var addedEmotionsInCompositeAnalyticsState = mutableStateMapOf<Emotions,Map<Long,Double>>()
        private set

    var compositeAnalyticsChartLegends = mutableStateMapOf<Emotions,LegendItem>()

    var compositeAnalyticsChartLineSpecs = mutableStateMapOf<Emotions,LineCartesianLayer.LineSpec>()


    // Aggregate Screen States
    var pieChartDataState by mutableStateOf(PieChartData(
            slices = emptyList<PieChartData.Slice>(),
            plotType = PlotType.Pie
    ))

    var aggregateEmotionSelectedState  = mutableMapOf<Emotions,AggregateScreenEmotionSelectedState>(
            Emotions.HAPPY to AggregateScreenEmotionSelectedState.SelectedAsDefault,
            Emotions.SAD to AggregateScreenEmotionSelectedState.SelectedAsDefault,
            Emotions.FEAR to AggregateScreenEmotionSelectedState.SelectedAsDefault,
            Emotions.ANGRY to AggregateScreenEmotionSelectedState.SelectedAsDefault,
            Emotions.DISGUST to AggregateScreenEmotionSelectedState.SelectedAsDefault,
            Emotions.NEUTRAL to AggregateScreenEmotionSelectedState.SelectedAsDefault,
            Emotions.SURPRISE to AggregateScreenEmotionSelectedState.SelectedAsDefault,
    )

    var totalDataPointsToConsider:Float = dataAnalyzer.totalDataPoint.toFloat();
    var totalPositiveDataPoints:Float by  mutableFloatStateOf(0f);
    var totalNegativeDataPoints:Float by  mutableFloatStateOf(0f);
    var totalDataPoints:Float by  mutableFloatStateOf(0f);



    // Starting video recording or capturing image
    fun onStart(controller: LifecycleCameraController,context: Context){

        when(cameraModeState){

            is CameraModeState.Video->{
                if(cameraModeState.recordingState is RecordingState.Started){
                    cameraModeState = CameraModeState.Video(RecordingState.Stopped)
                   camera.closeRecoding()
                    viewModelScope.launch(Dispatchers.Default){
                        updateSingleEmotionAnalyticModelState()
                    }

                }
                else{
                    cameraModeState = CameraModeState.Video(RecordingState.Started)
                    startTimeOfRecordingInSeconds=TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                    recordVideo(controller, context)

                }
            }

            is CameraModeState.Camera->{

                takePhoto(controller=controller,context=context)
            }
        }
    }
    fun changeCameraMode(){

        cameraModeState = when(cameraModeState){

            is CameraModeState.Camera->{
                CameraModeState.Video(RecordingState.Stopped)
            }

            is CameraModeState.Video->{
                CameraModeState.Camera()
            }
        }
    }

    private fun takePhoto(
        controller: LifecycleCameraController,
        context: Context
    ){

        camera.takePhoto(controller,context)
    }

    private fun recordVideo(
        controller: LifecycleCameraController,
        context: Context
    ){

        startTimeOfRecordingInSeconds=System.currentTimeMillis()
        camera.recordVideo(controller=controller, context = context)
    }


    fun changeEmotionDataInSingleEmotionAnalytic(emotion: Emotions){
        selectedEmotion =emotion
    }


    fun updateEmotionSumMap(emotionMap:Map<Int,Float>){

       emotionMap.forEach { (key,value)->

           emotionSumMap[key]= value + emotionSumMap.getOrDefault(key,0.0F);

       }
    }

    fun analyzeFrame(bitmapTimeStampWrapper: BitmapTimeStampWrapper){

        Log.d("analyzeFrame","${bitmapTimeStampWrapper.timeStampInSeconds} - ${startTimeOfRecordingInSeconds/1000} = ${ (bitmapTimeStampWrapper.timeStampInSeconds-startTimeOfRecordingInSeconds/1000)}")

        mlKitFaceDetector.getFacesFromCapturedImage(bitmapTimeStampWrapper.bitmap){

            dataAnalyzer.updateData(bitmapTimeStampWrapper.timeStampInSeconds-startTimeOfRecordingInSeconds/1000, it)
        }
    }

    suspend fun updateSingleEmotionAnalyticModelState(emotion: Emotions=Emotions.HAPPY){



        val emotionTimeSeries: Map<Long, List<Float>> =dataAnalyzer.getEmotionTimeSeriesData(emotion)

        val emotionTimeSeriesAverage = emotionTimeSeries.map {

            it.key to (it.value.average())

        }.toMap()


        val columnCharModel= CartesianChartModel(

                ColumnCartesianLayerModel.build {

                    series(x = emotionTimeSeriesAverage.keys, y =emotionTimeSeriesAverage.values)
                }
        )

        val lineCharModel= CartesianChartModel(

                LineCartesianLayerModel.build {

                    series(x = emotionTimeSeriesAverage.keys, y =emotionTimeSeriesAverage.values)
                }
        )

        chartModelState = chartModelState.copy(
                columnChartModel = columnCharModel,
                lineChartModel = lineCharModel
        )


    }


    suspend fun addEmotionDataInCompositeChart(emotion: Emotions) {



            val charSequence :CharSequence = emotion.toString()
            if (!addedEmotionsInCompositeAnalyticsState.keys.contains(emotion)) {

                compositeAnalyticsChartLegends[emotion] = LegendItem(
                                icon = LineComponent(
                                        color = emotion.color.toArgb(),

                                ),
                                labelText = charSequence,
                                label = TextComponent.build {
                                    this.color = Color.Black.toArgb()
                                }


                )




                compositeAnalyticsChartLineSpecs[emotion] =LineCartesianLayer.LineSpec(shader = DynamicShader.color(emotion.color),)



                val emotionTimeSeries: Map<Long, List<Float>> = dataAnalyzer.getEmotionTimeSeriesData(emotion)

                val emotionTimeSeriesAverage = emotionTimeSeries.map {

                    it.key to (it.value.average())

                }.toMap()

                addedEmotionsInCompositeAnalyticsState[emotion] = emotionTimeSeriesAverage

                compositeAnalyticsModel.tryRunTransaction {


                    lineSeries {
                        addedEmotionsInCompositeAnalyticsState.forEach {
                            series(
                                    x = it.value.keys,
                                    y = it.value.values
                            )
                        }

                    }
                }

            }



    }


    suspend fun removeEmotionFromCompositeChart(emotion: Emotions){


        if(addedEmotionsInCompositeAnalyticsState.keys.contains(emotion)){

           compositeAnalyticsChartLegends.remove(emotion)

            compositeAnalyticsChartLineSpecs.remove(emotion)


            addedEmotionsInCompositeAnalyticsState.remove(emotion)


        }



        compositeAnalyticsModel.tryRunTransaction {


            lineSeries {

                if(addedEmotionsInCompositeAnalyticsState.isEmpty()){


                    addedEmotionsInCompositeAnalyticsState.forEach {
                        series(
                                x = it.value.keys,
                                y = it.value.values
                        )
                    }
                }
                else{

                    series()
                }

            }
        }
    }

    fun onAggregateEmotionSelectedStateChanged(emotion: Emotions,state:AggregateScreenEmotionSelectedState){

        aggregateEmotionSelectedState[emotion]= state

        totalDataPoints=dataAnalyzer.totalDataPoint.toFloat()

    }

    fun aggregateResultShow(){



        totalPositiveDataPoints=0f
        totalNegativeDataPoints=0f
        totalDataPointsToConsider=0f

        aggregateEmotionSelectedState.forEach {

                var dataPoints =0;

                if(it.value is AggregateScreenEmotionSelectedState.SelectedAsPositive){
                    dataPoints =dataAnalyzer.getEmotionTotalDataPoints(emotion = it.key)
                    totalPositiveDataPoints+=dataPoints
                    totalDataPointsToConsider +=dataPoints
                }

                if(it.value is AggregateScreenEmotionSelectedState.SelectedAsNegative) {
                    dataPoints =dataAnalyzer.getEmotionTotalDataPoints(emotion = it.key)
                    totalNegativeDataPoints+=dataPoints
                    totalDataPointsToConsider +=dataPoints
                }

        }

        val positive =(totalPositiveDataPoints/totalDataPointsToConsider)*100
        val negative = (totalNegativeDataPoints/totalDataPointsToConsider)*100

        pieChartDataState=pieChartDataState.copy(
                slices =  listOf(
                        PieChartData.Slice(
                                label = "$positive %",
                                color = Color(0xff9bf09d),
                                value = positive,

                                ),

                        PieChartData.Slice(
                                label = "$negative %",
                                color = Color(0xfff68181),
                                value =negative,
                                sliceDescription = {
                                it.toString()
                            }
                        )
                )

        )
    }


}