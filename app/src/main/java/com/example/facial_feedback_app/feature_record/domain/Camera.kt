package com.example.facial_feedback_app.feature_record.domain

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class Camera @Inject constructor() {

    companion object{
        const val TAG="cameraClass"
    }
    private var recording:Recording?=null
    private var currentRecordingFile:Uri?=null

    /*
    * Capturing Images
    * */
    fun takePhoto(
        controller: LifecycleCameraController,
        context: Context
    ){
        controller.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback(){
                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        Log.e(TAG, exception.message.toString())

                    }

                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        Log.e(TAG, image.toString())
                    }

                }
        )
    }

    /*
    * closing the recording
    * and setting both recording and currentRecordingFile to null
    * */

    fun closeRecoding(){

        recording?.close()
        recording=null
        currentRecordingFile=null

    }

    @Throws(IOException::class)
   private fun createdVideoFile(context: Context):File{

       val currentTimeStamp=SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
           .format(Date())

       val storageDire:File?=context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)

       return  File.createTempFile(
               "VIDEO_${currentTimeStamp}",
               ".mp4",
               storageDire
       )


   }


    fun recordVideo(
        controller: LifecycleCameraController,
        context: Context
    ){

        try {

            val videoFile = createdVideoFile(context)

            currentRecordingFile = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        videoFile
                )

            val currentTimeStamp=SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(Date())
           val contentValues = ContentValues().apply {
               put(MediaStore.Video.Media.DISPLAY_NAME,"${currentTimeStamp}.mp4")
               put(MediaStore.Video.Media.MIME_TYPE,"video/mp4")

               val currentDate = LocalDate.now()
               val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
               put(MediaStore.Video.Media.DATE_TAKEN,currentDate.format(dateFormat))

           }

            val mediaStoreOptions= MediaStoreOutputOptions
                .Builder(
                    context.contentResolver,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    )
                .setContentValues(contentValues)
                .build()

            recording = controller

                .startRecording(
                    mediaStoreOptions,
                    AudioConfig.AUDIO_DISABLED,
                    ContextCompat.getMainExecutor(context)
            ) { videoRecordEvent ->

                Log.d("record", videoRecordEvent.recordingStats.toString())
                when (videoRecordEvent) {
                    is VideoRecordEvent.Finalize -> {
                        if (videoRecordEvent.hasError()) {

                            recording?.close()
                            recording = null


                        }
                    }


                }

            }
        }
        catch (e:IOException){
            Log.e(TAG
                  , "Error creating video file: ${e.message}", e)
        }
    }
}