package com.example.facial_feedback_app.feature_record.domain.classifer

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.util.Log
import com.example.facial_feedback_app.feature_record.domain.classifer.emotions.EmotionClassification
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.InterpreterFactory
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

abstract class EmotionClassifier(
        protected val assetManager: AssetManager,
        protected val modelName:String,
        protected val labels:List<String>,
        protected val numberThreads:Int=2,
        private val context: Context
) {
        private val TAG ="EmotionClassifier"
        protected lateinit var interpreter:InterpreterApi
        protected  val interpreterOptions:Interpreter.Options=Interpreter
                .Options()
                .setNumThreads(numberThreads)

        protected lateinit var classifier:EmotionClassification

        init {


                val fileDescriptor=assetManager.openFd(modelName)
                val inputStream = FileInputStream(fileDescriptor.fileDescriptor)

                val fileChannel= inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength= fileDescriptor.declaredLength

                try {
                        val assetManger = context.assets
                        val model = assetManager.openFd("simple_classifier.tflite")
                        val inputStream = FileInputStream(model.fileDescriptor)
                        val fileChannel= inputStream.channel

                        val so = model.startOffset
                        val dl=model.declaredLength

                        val mappedByteBuffer=fileChannel.map(FileChannel.MapMode.READ_ONLY,so,dl)

                        interpreter=InterpreterFactory().create(mappedByteBuffer,InterpreterApi.Options().setNumThreads(numberThreads))



                }
                catch (e:IllegalArgumentException){

                        Log.d("EmotionClassifier",e.message.toString())
                }


        }

        fun close() {
                interpreter.close()
        }

        @Throws(IOException::class)
        fun loadModel(modelFileName: String): MappedByteBuffer? {
                val fileDescriptor: AssetFileDescriptor = assetManager.openFd(modelFileName)
                val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
                val fileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }


        companion object{

                const val DELEGATE_CPU=0
                const val DELEGATE_GPU=1
        }
}