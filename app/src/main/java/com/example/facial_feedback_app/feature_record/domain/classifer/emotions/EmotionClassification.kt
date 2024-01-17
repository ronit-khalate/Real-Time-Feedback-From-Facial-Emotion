package com.example.facial_feedback_app.feature_record.domain.classifer.emotions

import com.example.facial_feedback_app.feature_record.domain.classifer.InterpreterParameters
import org.tensorflow.lite.InterpreterApi
import kotlin.properties.Delegates

class EmotionClassification(
        private val interpreter: InterpreterApi
):ClassifyEmotion {

    private var imageHeight by Delegates.notNull<Int>()
    private var imageWidth by Delegates.notNull<Int>()
    private var imageColorLength by Delegates.notNull<Int>()
    private var imageOutputLength by Delegates.notNull<Int>()
    init {
        imageHeight=InterpreterParameters.getInputImageHeight(interpreter)
        imageWidth=InterpreterParameters.getInputImageWidth(interpreter)
        imageColorLength=InterpreterParameters.getInputColorDimLength(interpreter)
        imageOutputLength=InterpreterParameters.getOutputLength(interpreter)

    }
    override fun classify(input: FloatArray): Array<FloatArray> {

        val formattedInput = Array(1) {
            Array(imageHeight) {
                Array(imageWidth) {
                    FloatArray(imageColorLength)
                }
            }
        }

       for(y in 0 until imageHeight){

           for (x in 0 until imageWidth){

               for (c in 0 until  imageColorLength){

                   formattedInput[0][y][x][c] = input[y * imageHeight + x * imageColorLength + c]
               }
           }
       }


        val outputArr = Array(1) {
            FloatArray(7)
        }

        interpreter.run(formattedInput,outputArr)

        return outputArr
    }
}