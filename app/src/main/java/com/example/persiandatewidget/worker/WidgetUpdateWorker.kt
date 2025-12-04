package com.example.persiandatewidget.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.persiandatewidget.widget.PersianDateWidget

class WidgetUpdateWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return runCatching {
            PersianDateWidget().updateAll(applicationContext)
            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }
}

