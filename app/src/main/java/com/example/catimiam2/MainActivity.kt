package com.example.catimiam2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnFeednow = findViewById<Button>(R.id.button2)
        val btnFeedlater = findViewById<Button>(R.id.button)

        btnFeednow.setOnClickListener(){

        }

        btnFeedlater.setOnClickListener(){

        }

    }


    private fun feedNotification(){
        val noitification = NotificationCompat.Builder(this,"c")
            .setSmallIcon(R.drawable.CAT)
            .setContentTitle("Cat Feeding Reminder")
            .setContentText("It's Time to feed your cat!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

    }
}