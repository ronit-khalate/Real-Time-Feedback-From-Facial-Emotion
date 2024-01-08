package com.example.facial_feedback_app.feature_record.presentation.camera.state

sealed class CameraModeState(open var recordingState: RecordingState){

    data class Camera(override var recordingState: RecordingState = RecordingState.clicked):
        CameraModeState(recordingState)
    data class Video(override var recordingState: RecordingState): CameraModeState(recordingState)
}