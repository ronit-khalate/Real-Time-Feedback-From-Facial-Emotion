package com.example.facial_feedback_app.feature_record.presentation

import com.example.facial_feedback_app.R

sealed class BottomNavItem(
        var title:String,
        var icon:Int,
        var screen_route:String

){
    object Image : BottomNavItem("Image", icon =R.drawable.photo_camera, "photo")

    object video : BottomNavItem("Video", R.drawable.videocam, "video")
    object storage: BottomNavItem("Storage", R.drawable.storage, "storage")
}
