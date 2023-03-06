package com.codemave.mobicomphw1.ui.home.notifications

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(
    context: Context,
    userParameters: WorkerParameters
) : Worker(context, userParameters) {

    override fun doWork(): Result {
        return try {
            println("Reminder fired")
            // Display the reminder in the main view
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}