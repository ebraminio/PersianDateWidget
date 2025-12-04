package com.example.persiandatewidget.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object WidgetUpdateHelper {

    private const val ALARM_ACTION = "com.example.persiandatewidget.WIDGET_UPDATE_ALARM"
    private const val HOURLY_INTERVAL = 60 * 60 * 1000L // 1 hour in milliseconds

    fun scheduleAlarmManagerUpdates(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WidgetUpdateAlarmReceiver::class.java).apply {
            action = ALARM_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule repeating alarm with setInexactRepeating for better battery life
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + HOURLY_INTERVAL,
            HOURLY_INTERVAL,
            pendingIntent
        )
    }

    fun cancelAlarmManagerUpdates(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WidgetUpdateAlarmReceiver::class.java).apply {
            action = ALARM_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}

class WidgetUpdateAlarmReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.persiandatewidget.WIDGET_UPDATE_ALARM") {
            scope.launch {
                PersianDateWidget().updateAll(context)
            }
        }
    }
}

