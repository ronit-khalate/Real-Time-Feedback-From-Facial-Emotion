package com.example.facial_feedback_app.feature_record.presentation.storage.state

import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel

data class SingleEmotionAnalyticsCharModels(
        val columnChartModel:CartesianChartModel?=null,
        val lineChartModel:CartesianChartModel?=null
)