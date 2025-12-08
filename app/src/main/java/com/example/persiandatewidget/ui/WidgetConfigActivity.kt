package com.example.persiandatewidget.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.compose
import androidx.glance.appwidget.updateAll
import com.example.persiandatewidget.R
import com.example.persiandatewidget.util.WidgetPreferences
import com.example.persiandatewidget.widget.PersianDateWidget
import com.google.android.glance.appwidget.host.AppWidgetHostPreview
import kotlinx.coroutines.launch

class WidgetConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = isSystemInDarkTheme()
            MaterialTheme(
                colorScheme = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                        val context = LocalContext.current
                        if (darkTheme) {
                            dynamicDarkColorScheme(context)
                        } else {
                            dynamicLightColorScheme(context)
                        }
                    }

                    darkTheme -> darkColorScheme()
                    else -> lightColorScheme()
                },
            ) { WidgetConfigScreen() }
        }
    }

    @Composable
    private fun WidgetConfigScreen() {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val dataStore by WidgetPreferences.dataStore(this)
        val useColorful = WidgetPreferences.isColorful(dataStore)
        val showAppName = WidgetPreferences.showAppName(dataStore)
        val backgroundAlpha = WidgetPreferences.getBackgroundAlpha(dataStore)
        val cornerRadius = WidgetPreferences.getCornerRadius(dataStore)
        val verticalPadding = WidgetPreferences.getVerticalPadding(dataStore)
        val horizontalPadding = WidgetPreferences.getHorizontalPadding(dataStore)
        val defaultHeight = 120.dp

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class) TopAppBar(
                    title = { Text(stringResource(R.string.config_title)) },
                )
            },
            containerColor = Color.Transparent,
        ) { paddingValues ->
            val surfaceColor = MaterialTheme.colorScheme.surface
            val layoutDirection = LocalLayoutDirection.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .drawBehind {
                        drawRect(color = surfaceColor)
                        val topPadding = paddingValues.calculateTopPadding()
                        val leftPadding = paddingValues.calculateLeftPadding(layoutDirection)
                        val rightPadding = paddingValues.calculateRightPadding(layoutDirection)
                        val cornerRadius = 16.dp.toPx()
                        val cardSide = 24.dp.toPx()
                        val cardWidth =
                            this.size.width - cardSide * 2 - (leftPadding + rightPadding).toPx()
                        val cardHeight = (defaultHeight + 48.dp).toPx()
                        val cardTop = (16.dp + topPadding).toPx()
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.05f * 4),
                            topLeft = Offset(
                                x = cardSide - 2 + leftPadding.toPx(),
                                y = cardTop + 2,
                            ),
                            size = Size(
                                width = cardWidth + 4,
                                height = cardHeight + 2,
                            ),
                            cornerRadius = CornerRadius(cornerRadius + 2),
                            style = Fill
                        )
                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = Offset(x = cardSide + leftPadding.toPx(), y = cardTop),
                            size = Size(width = cardWidth, height = cardHeight),
                            cornerRadius = CornerRadius(cornerRadius),
                            blendMode = BlendMode.Clear
                        )
                    }
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    val context = LocalContext.current
                    val widgetWidth = defaultHeight * 2
                    key(useColorful, showAppName, backgroundAlpha, cornerRadius, verticalPadding, horizontalPadding) {
                        AppWidgetHostPreview(
                            modifier = Modifier,
                            displaySize = DpSize(widgetWidth, defaultHeight)
                        ) {
                            PersianDateWidget().compose(
                                context = context,
                                size = DpSize(widgetWidth, defaultHeight),
                                state = null
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.preferences_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                        HorizontalDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    scope.launch {
                                        WidgetPreferences.setColorful(
                                            this@WidgetConfigActivity,
                                            !useColorful,
                                        )
                                        PersianDateWidget().updateAll(this@WidgetConfigActivity)
                                    }
                                }
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.pref_colorful),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = stringResource(R.string.pref_colorful_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                                )
                            }
                            Switch(
                                checked = useColorful,
                                onCheckedChange = null, // Handled by Row click
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.pref_vertical_padding),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = stringResource(R.string.pref_vertical_padding_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.compact),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                        alpha = 0.6f
                                    )
                                )
                                Slider(
                                    value = verticalPadding,
                                    onValueChange = { value ->
                                        scope.launch {
                                            WidgetPreferences.setVerticalPadding(
                                                this@WidgetConfigActivity,
                                                value,
                                            )
                                            PersianDateWidget().updateAll(this@WidgetConfigActivity)
                                        }
                                    },
                                    valueRange = 0f..24f,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = stringResource(R.string.spacious),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                        alpha = 0.6f
                                    )
                                )
                            }
                            Text(
                                text = "${verticalPadding.toInt()}dp",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.pref_horizontal_padding),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = stringResource(R.string.pref_horizontal_padding_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.compact),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                        alpha = 0.6f
                                    )
                                )
                                Slider(
                                    value = horizontalPadding,
                                    onValueChange = { value ->
                                        scope.launch {
                                            WidgetPreferences.setHorizontalPadding(
                                                this@WidgetConfigActivity,
                                                value,
                                            )
                                            PersianDateWidget().updateAll(this@WidgetConfigActivity)
                                        }
                                    },
                                    valueRange = 0f..24f,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = stringResource(R.string.spacious),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                        alpha = 0.6f
                                    )
                                )
                            }
                            Text(
                                text = "${horizontalPadding.toInt()}dp",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    scope.launch {
                                        WidgetPreferences.setShowAppName(
                                            this@WidgetConfigActivity,
                                            !showAppName,
                                        )
                                        PersianDateWidget().updateAll(this@WidgetConfigActivity)
                                    }
                                }
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.pref_show_app_name),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = stringResource(R.string.pref_show_app_name_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                                )
                            }
                            Switch(
                                checked = showAppName,
                                onCheckedChange = null, // Handled by Row click
                            )
                        }

                        AnimatedVisibility(
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S || !useColorful
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.pref_background_opacity),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = stringResource(R.string.pref_background_opacity_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.transparent),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                    Slider(
                                        value = backgroundAlpha,
                                        onValueChange = { value ->
                                            scope.launch {
                                                WidgetPreferences.setBackgroundAlpha(
                                                    this@WidgetConfigActivity,
                                                    value,
                                                )
                                                PersianDateWidget().updateAll(this@WidgetConfigActivity)
                                            }
                                        },
                                        valueRange = 0f..1f,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = stringResource(R.string.opaque),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                }
                                Text(
                                    text = "${(backgroundAlpha * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        AnimatedVisibility(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.pref_corner_radius),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = stringResource(R.string.pref_corner_radius_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.sharp),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                    Slider(
                                        value = cornerRadius,
                                        onValueChange = { value ->
                                            scope.launch {
                                                WidgetPreferences.setCornerRadius(
                                                    this@WidgetConfigActivity,
                                                    value,
                                                )
                                                PersianDateWidget().updateAll(this@WidgetConfigActivity)
                                            }
                                        },
                                        valueRange = 0f..60f,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = stringResource(R.string.rounded),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                }
                                Text(
                                    text = "${cornerRadius.toInt()}dp",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }

//                FilledTonalButton(
//                    onClick = {
//                        scope.launch {
//                            runCatching {
//                                PersianDateWidget().updateAll(this@WidgetConfigActivity)
//                                snackbarHostState.showSnackbar(getString(R.string.message_widget_updated))
//                            }.onFailure {
//                                snackbarHostState.showSnackbar(getString(R.string.message_update_error))
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                ) {
//                    Text(
//                        text = stringResource(R.string.button_refresh_widget),
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }

                FilledTonalButton(
                    onClick = { finish() }, modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) { Text(text = stringResource(R.string.button_close), fontSize = 16.sp) }
            }
        }
    }
}
