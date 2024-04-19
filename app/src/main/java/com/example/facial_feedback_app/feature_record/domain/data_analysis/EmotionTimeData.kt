package com.example.facial_feedback_app.feature_record.domain.data_analysis


/*
* This data class represents the emotion captured at give time
*
*
* */
data class EmotionTimeData(
        val time:Long,
        val emotions:List<Long>
)