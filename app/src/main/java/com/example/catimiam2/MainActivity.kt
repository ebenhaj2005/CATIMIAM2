package com.example.catimiam2

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {




    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnHistory = findViewById<Button>(R.id.button4)
        val btnFeednow = findViewById<Button>(R.id.button2)
        val btnFeedlater = findViewById<Button>(R.id.button)
        val foodInput = findViewById<EditText>(R.id.food_input)
val sharedPreferences = getSharedPreferences("FeedingHistory", MODE_PRIVATE)
   val editor = sharedPreferences.edit()
        val feedingHistory = sharedPreferences.getStringSet("history", mutableSetOf()) ?: mutableSetOf()



        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("feedingHistory",ArrayList(feedingHistory))
            startActivity(intent)
        }







        @SuppressLint("ScheduleExactAlarm")
        fun feedLaterNotification() {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, FeedReminderReceiver::class.java)

            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            val triggerTime = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 1)
            }.timeInMillis

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                } else {
                    try {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun feedNotification() {
            val notification = NotificationCompat.Builder(this, "cat_feed")
                .setSmallIcon(R.drawable.cat)
                .setContentTitle("Cat Feeding Reminder")
                .setContentText("It's time to feed your cat!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            with(NotificationManagerCompat.from(this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            1
                        )
                        return
                    }
                    notify(1, notification)
                }
            }
        }


        @SuppressLint("DefaultLocale")
        fun getCurrentTime(): String {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }

        btnFeednow.setOnClickListener {
            feedNotification()
            val foodGiven = foodInput.text.toString().trim()
            if (foodGiven.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter the food given to the cat",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val currentTime = getCurrentTime()
                feedingHistory.add("$foodGiven at $currentTime")
                Toast.makeText(this, "$foodGiven recorded at $currentTime", Toast.LENGTH_SHORT)
                    .show()

                editor.putStringSet("history",feedingHistory)
                editor.apply()
                foodInput.text.clear()
            }
        }

        btnFeedlater.setOnClickListener {
            feedLaterNotification()
        }
    }
}