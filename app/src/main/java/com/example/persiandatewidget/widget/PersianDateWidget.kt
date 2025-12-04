package com.example.persiandatewidget.widget

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.persiandatewidget.R
import com.example.persiandatewidget.util.PersianDate
import com.example.persiandatewidget.util.WidgetPreferences

class PersianDateWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) =
        provideContent { GlanceTheme { PersianDateWidgetContent(context) } }

    @Composable
    private fun PersianDateWidgetContent(context: Context) {
        val dataStore by WidgetPreferences.dataStore(context)
        val useColorful = WidgetPreferences.isColorful(dataStore)
        val transparentBackground = WidgetPreferences.isTransparent(dataStore)

        if (!transparentBackground && !useColorful) AndroidRemoteViews(
            RemoteViews(context.packageName, R.layout.widget_backround),
            GlanceModifier.fillMaxSize()
        )
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .then(
                    if (!transparentBackground && useColorful) GlanceModifier.background(
                        GlanceTheme.colors.widgetBackground
                    ) else GlanceModifier
                )
                .padding(4.dp)
                .clickable(onClick = actionRunCallback<OpenCalendarAction>()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val persianDate = PersianDate()
            Text(
                text = persianDate.dayOfMonth,
                style = TextStyle(
                    color = if (useColorful) {
                        GlanceTheme.colors.primary
                    } else {
                        GlanceTheme.colors.onBackground
                    },
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )

            Text(
                text = persianDate.monthName,
                style = TextStyle(
                    color = if (useColorful) {
                        GlanceTheme.colors.onSurfaceVariant
                    } else {
                        GlanceTheme.colors.onBackground
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                ),
                modifier = GlanceModifier.padding(top = 2.dp),
            )
        }
    }
}

