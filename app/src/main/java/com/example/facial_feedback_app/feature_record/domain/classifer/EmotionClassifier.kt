package com.example.facial_feedback_app.feature_record.domain.classifer

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.example.facial_feedback_app.feature_record.domain.classifer.emotions.EmotionClassification
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.InterpreterFactory
import java.io.FileInputStream
import java.nio.channels.FileChannel

abstract class EmotionClassifier(
        assetManager: AssetManager,
        protected val modelName:String,
        protected val labels:List<String>,
        protected val numberThreads:Int=2,
        private val context: Context
) {
        private val TAG ="EmotionClassifier"
        protected lateinit var interpreter:InterpreterApi
        protected  val interpreterOptions:InterpreterApi.Options=InterpreterApi
                .Options()
                .setNumThreads(numberThreads)

        protected lateinit var classifier:EmotionClassification


        init {


                try {

                        val model = assetManager.openFd(modelName)
                        val inputStream = FileInputStream(model.fileDescriptor)
                        val fileChannel= inputStream.channel

                        val so = model.startOffset
                        val dl=model.declaredLength

                        val mappedByteBuffer=fileChannel.map(FileChannel.MapMode.READ_ONLY,so,dl)

                        interpreter=InterpreterFactory().create(mappedByteBuffer,interpreterOptions)



                }
                catch (e:IllegalArgumentException){

                        Log.d("EmotionClassifier",e.message.toString())
                }


        }

        fun close() {
                interpreter.close()
        }
}