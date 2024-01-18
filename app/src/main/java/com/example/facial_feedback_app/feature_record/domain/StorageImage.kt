package com.example.facial_feedback_app.feature_record.domain

import android.graphics.Bitmap

data  class StorageImage(
        val image: Bitmap,
        val emotion:Map<String,Float>
)