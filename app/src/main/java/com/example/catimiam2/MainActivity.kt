package com.example.catimiam2

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {




    @SuppressLint("MutatingSharedPrefs")
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

            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else
                    PendingIntent.FLAG_UPDATE_CURRENT
            )


            val triggerTime = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 30)
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



        @SuppressLint("ScheduleExactAlarm")
        fun feedNowButtonNotification() {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, FeedReminderReceiver::class.java)

            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else
                    PendingIntent.FLAG_UPDATE_CURRENT
            )


            val triggerTime = Calendar.getInstance().apply {
                add(Calendar.HOUR, 3)
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



        btnFeedlater.setOnClickListener {
            feedLaterNotification()
        }


         fun createNotifcationChannel(){
           if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
               val channel = NotificationChannel(
                   "cat_feed",
                   "Cat Feeding Notifications",
                   NotificationManager.IMPORTANCE_HIGH
               ).apply {
                   description = "Reminders to feed your cat"
               }
               val notificationManager : NotificationManager =
                   getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
               notificationManager.createNotificationChannel(channel)
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
            feedNowButtonNotification()
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


    }
}