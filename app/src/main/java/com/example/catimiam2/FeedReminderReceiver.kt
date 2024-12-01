package com.example.catimiam2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class FeedReminderReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return // Ensure context is not null

        val notification = NotificationCompat.Builder(context, "cat_feed")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Reminder!")
            .setContentText("It's time to feed your cat again.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (checkNotificationPermission(context)) {
                notify(2, notification)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission(context: Context): Boolean {
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        return androidx.core.content.ContextCompat.checkSelfPermission(
            context, permission
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}
