package com.example.facial_feedback_app.feature_record.domain.classifer.emotions

interface ClassifyEmotion {

    fun classify(input:FloatArray):Array<FloatArray>
}