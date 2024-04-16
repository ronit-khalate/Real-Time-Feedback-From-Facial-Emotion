package com.example.facial_feedback_app.feature_record.domain.classifer

import org.tensorflow.lite.InterpreterApi

class InterpreterParameters {


    companion object{
        private const val IMAGE_INPUT_TENSOR_INDEX = 0

        private const val IMAGE_OUTPUT_TENSOR_INDEX = 0

        // Indices of an input image parameters
        private const val MODEL_INPUT_WIDTH_INDEX = 1
        private const val MODEL_INPUT_HEIGHT_INDEX = 2
        private const val MODEL_INPUT_COLOR_DIM_INDEX = 3


        // Index of an output result array
        private const val MODEL_OUTPUT_LENGTH_INDEX = 1

        fun getInputImageWidth(interpreter: InterpreterApi): Int {
            return interpreter.getInputTensor(IMAGE_INPUT_TENSOR_INDEX)
                .shape()[MODEL_INPUT_WIDTH_INDEX]
        }

        fun getInputImageHeight(interpreter: InterpreterApi): Int {
            return interpreter.getInputTensor(IMAGE_INPUT_TENSOR_INDEX)
                .shape()[MODEL_INPUT_HEIGHT_INDEX]
        }

        fun getInputColorDimLength(interpreter: InterpreterApi): Int {
            return interpreter.getInputTensor(IMAGE_INPUT_TENSOR_INDEX)
                .shape()[MODEL_INPUT_COLOR_DIM_INDEX]
        }

        fun getOutputLength(interpreter: InterpreterApi): Int {
            return interpreter.getInputTensor(IMAGE_OUTPUT_TENSOR_INDEX)
                .shape() [MODEL_OUTPUT_LENGTH_INDEX]
        }
    }
}