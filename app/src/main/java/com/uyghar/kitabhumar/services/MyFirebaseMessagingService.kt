package com.uyghar.kitabhumar.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.i("firebase_token", p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }
}