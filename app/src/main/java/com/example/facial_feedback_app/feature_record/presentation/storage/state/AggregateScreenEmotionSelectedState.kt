package com.example.facial_feedback_app.feature_record.presentation.storage.state

sealed interface AggregateScreenEmotionSelectedState{

    object SelectedAsDefault:AggregateScreenEmotionSelectedState
    object SelectedAsPositive:AggregateScreenEmotionSelectedState

    object SelectedAsNegative:AggregateScreenEmotionSelectedState
}
