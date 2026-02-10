package com.manraap.permanentlearning.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.manraap.permanentlearning.R
import com.manraap.permanentlearning.data.AppDatabase

class DueRevisionWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val dueTopics = AppDatabase.getInstance(applicationContext)
            .topicDao()
            .getDueTopics(System.currentTimeMillis())

        if (dueTopics.isNotEmpty()) {
            createChannelIfNeeded()
            if (hasNotificationPermission()) {
                val text = "${dueTopics.size} topic(s) due. 2 minutes of recall now prevents weeks of forgetting."
                val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_brain)
                    .setContentTitle("Revision due")
                    .setContentText(text)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()
                NotificationManagerCompat.from(applicationContext).notify(1001, notification)
            }
        }
        return Result.success()
    }

    private fun createChannelIfNeeded() {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, "Due revisions", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }

    private fun hasNotificationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CHANNEL_ID = "due_revisions"
    }
}
