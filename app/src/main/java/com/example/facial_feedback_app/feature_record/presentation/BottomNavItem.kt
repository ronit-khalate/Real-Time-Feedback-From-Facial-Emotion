package com.example.facial_feedback_app.feature_record.presentation

import com.example.facial_feedback_app.R

sealed class BottomNavItem(
        var title:String,
        var icon:Int,
        var screen_route:String

){
    object Camera : BottomNavItem(title = "Image", icon =R.drawable.photo_camera, "photo")

    object Analytics: BottomNavItem(title = "Storage" ,icon= R.drawable.storage, "storage")
}
