package com.example.facial_feedback_app.feature_record.domain

import android.graphics.Bitmap

data class ImageEmotion(
        val bitmap: Bitmap,
        val emotionMap:Map<String,Float>
)
