package com.example.facial_feedback_app.core.di

import android.content.Context
import com.example.facial_feedback_app.core.Constants
import com.example.facial_feedback_app.feature_record.domain.classifer.EmotionClassifierImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun emotionClassifier(@ApplicationContext context: Context):EmotionClassifierImpl{

        return  EmotionClassifierImpl(
                assetManager = context.assets,
                modelName = Constants.MODEL_NAME,
                labels = Constants.emotionList,
                context = context
        )
    }
}