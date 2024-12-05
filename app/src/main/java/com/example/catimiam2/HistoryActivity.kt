package com.example.catimiam2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity()  {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val btnHistory = findViewById<Button>(R.id.button3)
        btnHistory.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val historyTextView = findViewById<TextView>(R.id.textView3)




        val sharedPreferences = getSharedPreferences("FeedingHistory", MODE_PRIVATE)
        val feedingHistory = sharedPreferences.getStringSet("history", mutableSetOf())

        if (feedingHistory != null) {
         historyTextView.text = feedingHistory.joinToString(separator = "\n")

            }else {
                historyTextView.text = "No feeding history available"
            }
        }


}
