package com.example.facial_feedback_app.feature_record.domain

import android.graphics.Rect

data class FaceBoundInfo(
        val x:Int,
        val y:Int,
        val width:Int,
        val height:Int,
        val boundingBox:Rect
)