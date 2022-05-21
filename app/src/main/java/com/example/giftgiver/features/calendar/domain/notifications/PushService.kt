package com.example.giftgiver.features.calendar.domain.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushService : FirebaseMessagingService() {
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Log.d("FirebaseMessaging", "Refreshed token: $newToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notificationService = NotificationService(this)
        remoteMessage.notification?.title?.let { notificationService.showNotification(it) }
    }
}