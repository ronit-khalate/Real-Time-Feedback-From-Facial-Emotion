package com.example.facial_feedback_app.feature_record.presentation.storage.state

import com.example.facial_feedback_app.feature_record.domain.Emotions
import com.patrykandpatrick.vico.core.common.LegendItem

data class CompositeAnalyticsScreenState(
        val legendList:List<LegendItem> = emptyList(),
        val addedEmotion:MutableMap<Emotions,Map<Long , Double>> = mutableMapOf()
) {
}