package com.josemgu91.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import com.josemgu91.alarms.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.buttonProgramAlarm.setOnClickListener {
            try {
                val timeInSecondsString = viewBinding.editTextTimeInSeconds.text.toString()
                val timeInSeconds = timeInSecondsString.toInt()
                programAlarm(timeInSeconds)
                showMessage(R.string.status_alarm_programmed)
            } catch (e: NumberFormatException) {
                showMessage(R.string.error_no_number)
            }
        }
    }

    private fun programAlarm(timeInSeconds: Int) {
        val alarmManager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val operation = PendingIntent.getService(
            this,
            1,
            Intent(this, AlarmService::class.java)
                .putExtra(AlarmService.ARG_COMMAND, AlarmService.COMMAND_START_ALARM),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        setExactAlarm(alarmManager, timeInSeconds, operation)
    }

    private fun setExactAlarm(
        alarmManager: AlarmManager,
        timeInSeconds: Int,
        operation: PendingIntent
    ) {
        val triggerAtMillis =
            SystemClock.elapsedRealtime() + timeInSeconds * 1000
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            operation
        )
    }

    private fun showMessage(@StringRes stringRes: Int) {
        showMessage(getString(stringRes))
    }

    private fun showMessage(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}