package com.example.catimiam2



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class FeedReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            showNotification(it)
        }
    }

    private fun showNotification(context: Context) {
        val channelId = "feed_reminder_channel"
        val notificationId = 1


        val resultIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            resultIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else
                PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Feed Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications to remind you to feed"
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("CATIMIAM")
            .setContentText("Time to feed your cat!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()


        notificationManager.notify(notificationId, notification)
    }
}
