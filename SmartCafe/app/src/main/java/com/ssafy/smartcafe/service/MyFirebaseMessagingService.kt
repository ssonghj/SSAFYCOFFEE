package com.ssafy.smartcafe.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Override
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Log", "onNewToken: ${token}")
    }
}