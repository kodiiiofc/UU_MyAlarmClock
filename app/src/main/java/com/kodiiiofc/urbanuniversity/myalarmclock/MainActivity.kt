package com.kodiiiofc.urbanuniversity.myalarmclock

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var alarmsLV: ListView
    private lateinit var createBTN: Button

    private val alarms: MutableList<String> = mutableListOf()
    private var calendar: Calendar? = null
    private var materialTimePicker: MaterialTimePicker? = null
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        alarmsLV = findViewById(R.id.lv_alarms)
        createBTN = findViewById(R.id.btn_create)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, alarms)
        alarmsLV.adapter = adapter

        createBTN.setOnClickListener {
            materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Выберите время будильника")
                .build()
            materialTimePicker!!.addOnPositiveButtonClickListener {
                calendar = Calendar.getInstance()
                calendar?.set(Calendar.SECOND, 0)
                calendar?.set(Calendar.MILLISECOND, 0)
                calendar?.set(Calendar.MINUTE, materialTimePicker!!.minute)
                calendar?.set(Calendar.HOUR_OF_DAY, materialTimePicker!!.hour)

                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

                alarmManager.setExact(
                    RTC_WAKEUP,
                    calendar!!.timeInMillis,
                    getAlarmPendingIntent()!!
                )

                alarms.add(dateFormat.format(calendar!!.time))
                adapter.notifyDataSetChanged()

                Toast.makeText(
                    this,
                    "Будильник создан на ${dateFormat.format(calendar!!.time)}",
                    Toast.LENGTH_SHORT
                ).show()


            }
            materialTimePicker!!.show(supportFragmentManager, "TAG_picker")


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> {
                Toast.makeText(applicationContext, "Приложение закрыто", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun getAlarmPendingIntent(): PendingIntent? {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getBroadcast(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}