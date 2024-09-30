package com.kodiiiofc.urbanuniversity.myalarmclock

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class AlarmActivity : AppCompatActivity() {

    private lateinit var stopBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        stopBTN = findViewById(R.id.btn_stop)

        stopBTN.setOnClickListener {
            finish()
            exitProcess(0)
        }

    }
}