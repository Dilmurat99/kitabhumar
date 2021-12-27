package com.uyghar.kitabhumar.services

import android.util.Log
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        subscribeToTopic()
        Log.i("firebase_token", p0)
    }

    fun subscribeToTopic() {
        Firebase.messaging.subscribeToTopic("kitabhumar")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i("subscribe", "failed")
                }

                Toast.makeText(baseContext, "subscribe ok", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val data = p0.data
        val id = data["id"]

        print(id)
    }
}