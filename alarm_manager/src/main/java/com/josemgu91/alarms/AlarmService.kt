package com.josemgu91.alarms

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AlarmService : Service() {

    private var ringtone: Ringtone? = null

    override fun onCreate() {
        super.onCreate()
        initRingtone()
    }

    private fun initRingtone() {
        ringtone = RingtoneManager.getRingtone(
            this,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getStringExtra(ARG_COMMAND)) {
            COMMAND_START_ALARM -> {
                showNotification()
                ringtone?.play()
            }
            COMMAND_STOP_ALARM -> ringtone?.stop()
        }
        return START_NOT_STICKY
    }

    private fun showNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ALARM_ID,
                    getString(R.string.alarm_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        notificationManager.notify(NOTIFICATION_ID, buildNotification())
    }

    private fun buildNotification(): Notification? {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ALARM_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.alar_notification_content))
            .setDeleteIntent(
                PendingIntent.getService(
                    this,
                    0,
                    Intent(this, AlarmService::class.java)
                        .putExtra(ARG_COMMAND, COMMAND_STOP_ALARM),
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
            .build()
    }

    companion object {

        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ALARM_ID = "alarm_channel"

        const val ARG_COMMAND = "alarmService.command"
        const val COMMAND_STOP_ALARM = "alarmService.stop_alarm"
        const val COMMAND_START_ALARM = "alarmService.start_alarm"
    }

}