package com.example.giftgiver.features.calendar.domain.notifications

import android.content.Context
import androidx.work.ListenableWorker.Result.failure
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifyWorker(
    context: Context,
    params: WorkerParameters
) :
    Worker(context, params) {
    override fun doWork(): Result {
        try {
            val notificationService = NotificationService()
            inputData.getString(EVENT_NAME)?.let {
                notificationService.showNotification(it)
            }
        } catch (ex: Exception) {
            return failure()
        }
        return success()
    }

    companion object {
        const val EVENT_NAME = "EVENT_NAME"
    }
}
