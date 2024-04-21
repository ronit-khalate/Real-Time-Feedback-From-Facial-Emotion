package com.example.facial_feedback_app.core.di

import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {


    @Provides
    fun provideCartesianChartModelProducer():CartesianChartModelProducer=CartesianChartModelProducer.build()

    @Provides
    fun provideCartesianMode():CartesianChartModel = CartesianChartModel()

}