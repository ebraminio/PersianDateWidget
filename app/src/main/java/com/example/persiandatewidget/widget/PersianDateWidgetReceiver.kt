package com.example.persiandatewidget.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.persiandatewidget.worker.WidgetUpdateWorker
import java.util.concurrent.TimeUnit

class PersianDateWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = PersianDateWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        scheduleWidgetUpdates(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        WidgetUpdateHelper.scheduleAlarmManagerUpdates(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelWidgetUpdates(context)
        WidgetUpdateHelper.cancelAlarmManagerUpdates(context)
    }

    private fun scheduleWidgetUpdates(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            1, TimeUnit.HOURS,
            15, TimeUnit.MINUTES // flex interval
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WIDGET_UPDATE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun cancelWidgetUpdates(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WIDGET_UPDATE_WORK_NAME)
    }

    companion object {
        private const val WIDGET_UPDATE_WORK_NAME = "persian_date_widget_update"
    }
}

