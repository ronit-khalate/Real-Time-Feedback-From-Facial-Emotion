package com.example.facial_feedback_app.feature_record.domain.classifer

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import com.example.facial_feedback_app.feature_record.domain.classifer.emotions.EmotionClassification
import com.example.facial_feedback_app.utils.toGrayscale
import java.util.Formatter
import javax.inject.Inject

class EmotionClassifierImpl @Inject constructor(
    assetManager: AssetManager,
    modelName:String,
    labels:List<String>,
    context: Context

):EmotionClassifier(assetManager, modelName, labels,context=context) {


    init {
        classifier= EmotionClassification(interpreter)
    }
    fun classify(imageBitmap: Bitmap,useFilter: Boolean):FloatArray{

        val preProcessedImage = preProcessImage(imageBitmap,useFilter)

        return classify(preProcessedImage)


    }

    private fun classify(input:FloatArray): FloatArray {
        val output = classifier.classify(input)

        // Checked compliance with the array of strings specified in the constructor
        if (labels.size != output[0].size) {
            val formatter = Formatter()
            throw IllegalArgumentException(
                    formatter.format(
                            "labels array length must be equal to %1\$d, but actual length is %2\$d",
                            output[0].size,
                            labels.size
                    ).toString()
            )
        }

        val outputMap = mutableMapOf<String,Float>()
        var predictedLabel:String

        var probability:Float

        for( i in 0 until output[0].size){
            predictedLabel=labels[i]

            probability=output[0][i]

            outputMap[predictedLabel] = probability

        }

        return output[0]
    }



    private fun preProcessImage(imageBitmap: Bitmap,useFilter: Boolean):FloatArray{

        val scaledAndGreyscaleImage = Bitmap.createScaledBitmap(
                imageBitmap,
                InterpreterParameters.getInputImageWidth(interpreter),
                InterpreterParameters.getInputImageHeight(interpreter),
                useFilter
        )

        val greyScaled=scaledAndGreyscaleImage.toGrayscale()

        val preprocessedImage = FloatArray(greyScaled.size)

        (preprocessedImage.indices).forEach { index->

            preprocessedImage[index]=greyScaled[index]/255.0f

        }

        return preprocessedImage


    }

}