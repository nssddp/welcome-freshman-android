package com.example.welcome_freshman.core.notification

import android.Manifest.permission
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.welcome_freshman.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 *@date 2024/3/14 9:57
 *@author GFCoder
 */

private const val TASK_NOTIFICATION_CHANNEL_ID = ""

@Singleton
class SystemNotifier @Inject constructor(
    @ApplicationContext private val context: Context,

    ) : Notifier {
    override fun postTaskNotification() = with(context) {
        if (checkSelfPermission(permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            return
        }

        val tasksNotifications = createTasksNotification {
            setContentTitle("Task Reminder")
                .setContentText("There is a task to be completed here")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
        }

        // Send the notifications
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(Random.nextInt(), tasksNotifications)

    }

}

private fun Context.createTasksNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        TASK_NOTIFICATION_CHANNEL_ID,
    )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .apply(block)
        .build()
}

private fun Context.ensureNotificationChannelExists() {

    val channel = NotificationChannel(
        TASK_NOTIFICATION_CHANNEL_ID,
        getString(R.string.core_notifications_tasks_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = getString(R.string.core_notifications_tasks_notification_channel_description)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}