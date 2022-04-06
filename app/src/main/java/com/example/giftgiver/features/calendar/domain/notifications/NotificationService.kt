package com.example.giftgiver.features.calendar.domain.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.giftgiver.R

class NotificationService(private val context: Context) {
    private val manager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val pattern = arrayOf(100L, 200L, 0, 400L).toLongArray()

    fun showNotification(title: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.app_name),
                IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notific_channel_desc)
                lightColor = Color.BLUE
                vibrationPattern = pattern
            }.also {
                manager.createNotificationChannel(it)
            }
        }

        val ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon =
            AppCompatResources.getDrawable(context, R.mipmap.ic_gift_giver_round)?.toBitmap()
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setLargeIcon(largeIcon)
            .setSmallIcon(R.drawable.ic_baseline_calendar_month_24)
            .setContentTitle(context.getString(R.string.important_day))
            .setShowWhen(true)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(title))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(ringURI)

        manager.notify(1, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "channel_id_1"
    }
}
