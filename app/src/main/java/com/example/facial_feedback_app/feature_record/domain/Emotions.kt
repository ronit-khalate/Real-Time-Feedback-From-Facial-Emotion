package com.example.facial_feedback_app.feature_record.domain

import androidx.compose.ui.graphics.Color

enum class Emotions(val color: Color) {

    /*
    * Ordinals of this enum objects will be index in emotion array
    * */
    ANGRY(Color.Red),
    DISGUST(Color.Green),
    FEAR(Color.Black),
    HAPPY(Color.Yellow),
    NEUTRAL(Color.Gray),
    SAD(Color.Blue),
    SURPRISE(Color.Magenta)
}