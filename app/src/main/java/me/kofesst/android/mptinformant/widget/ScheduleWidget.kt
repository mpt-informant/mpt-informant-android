package me.kofesst.android.mptinformant.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import kotlinx.serialization.decodeFromString
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.theme.color
import me.kofesst.android.mptinformant.ui.theme.md_theme_dark_onSurface
import me.kofesst.android.mptinformant.ui.theme.md_theme_dark_surface
import me.kofesst.android.mptinformant.ui.uiText
import me.kofesst.android.mptinformant.widget.worker.ScheduleWorkerTask
import me.kofesst.android.mptinformer.domain.models.WeekLabel
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleDay
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleRow
import me.kofesst.android.mptinformer.domain.models.settings.WidgetSettings
import java.io.File

class ScheduleWidget : GlanceAppWidget() {
    companion object {
        val schedulePreferencesKey = stringPreferencesKey("schedule")
        val weekLabelPreferencesKey = stringPreferencesKey("week_label")
        val widgetSettingsPreferencesKey = stringPreferencesKey("widget_settings")
    }

    override val stateDefinition = ScheduleWidgetStateDefinition

    @Composable
    override fun Content() {
        val (schedule, weekLabel) = restoreScheduleState()
        val widgetSettings = restoreWidgetSettings()
        ScheduleWidgetLayout {
            if (schedule == null) {
                ScheduleWidgetError()
            } else {
                ScheduleWidgetContent(
                    settings = widgetSettings,
                    weekLabel = weekLabel,
                    schedule = schedule,
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
            }
        }
    }

    @Composable
    private fun restoreScheduleState(): Pair<GroupScheduleDay?, WeekLabel> {
        val state = currentState<Preferences>()
        val scheduleJson = state[schedulePreferencesKey]
        val schedule = if (scheduleJson != null) {
            ScheduleWorkerTask.json.decodeFromString<GroupScheduleDay>(scheduleJson)
        } else {
            null
        }
        val weekLabelJson = state[weekLabelPreferencesKey]
        val weekLabel = with(weekLabelJson?.toIntOrNull()) {
            if (this != null) {
                WeekLabel.values()[this]
            } else {
                WeekLabel.None
            }
        }
        return schedule to weekLabel
    }

    @Composable
    private fun restoreWidgetSettings(): WidgetSettings {
        val state = currentState<Preferences>()
        val settingsJson = state[widgetSettingsPreferencesKey]
        return if (settingsJson != null) {
            ScheduleWorkerTask.json.decodeFromString(settingsJson)
        } else {
            WidgetSettings()
        }
    }

    @Composable
    private fun ScheduleWidgetLayout(
        content: @Composable ColumnScope.() -> Unit,
    ) {
        Column(
            content = content,
            verticalAlignment = Alignment.Vertical.CenterVertically,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(md_theme_dark_surface),
        )
    }

    @Composable
    private fun ScheduleWidgetError() {
        Text(
            text = ResourceString.scheduleWidgetUpdatesSoon.asGlanceString(),
            style = TextStyle(
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }

    @Composable
    private fun ScheduleWidgetContent(
        modifier: GlanceModifier = GlanceModifier,
        settings: WidgetSettings,
        schedule: GroupScheduleDay,
        weekLabel: WeekLabel,
    ) {
        Column(
            modifier = modifier,
        ) {
            ScheduleWidgetHeader(
                weekLabel = weekLabel,
                title = schedule.dayOfWeek.uiText().asGlanceString(),
                branch = schedule.branch,
                modifier = GlanceModifier.fillMaxWidth()
            )
            Spacer(
                modifier = GlanceModifier.height(20.dp)
            )
            LazyColumn(modifier = GlanceModifier.fillMaxWidth()) {
                items(schedule.rows) { scheduleRow ->
                    ScheduleRowContent(
                        settings = settings,
                        scheduleRow = scheduleRow,
                        weekLabel = weekLabel,
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun ScheduleWidgetHeader(
        modifier: GlanceModifier,
        weekLabel: WeekLabel,
        title: String,
        branch: String,
    ) {
        ColoredSurface(
            modifier = modifier,
            color = weekLabel.color()
        ) {
            Text(
                text = title,
                style = TextStyle(
                    color = ColorProvider(md_theme_dark_onSurface),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            if (branch.isNotBlank()) {
                Text(
                    text = branch,
                    style = TextStyle(
                        color = ColorProvider(md_theme_dark_onSurface),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }

    @Composable
    private fun ScheduleRowContent(
        modifier: GlanceModifier = GlanceModifier,
        settings: WidgetSettings,
        scheduleRow: GroupScheduleRow,
        weekLabel: WeekLabel,
    ) {
        when (scheduleRow) {
            is GroupScheduleRow.Single -> SingleScheduleRowContent(modifier, scheduleRow)
            is GroupScheduleRow.Divided -> DividedScheduleRowContent(
                settings = settings,
                scheduleRow = scheduleRow,
                weekLabel = weekLabel,
                modifier = modifier
            )
            else -> {}
        }
    }

    @Composable
    private fun SingleScheduleRowContent(
        modifier: GlanceModifier = GlanceModifier,
        scheduleRow: GroupScheduleRow.Single,
    ) {
        Row(modifier = modifier) {
            Text(
                text = ResourceString.lessonNumberFormat.asGlanceString(scheduleRow.lessonNumber),
                style = TextStyle(
                    color = ColorProvider(md_theme_dark_onSurface),
                    fontSize = 16.sp
                )
            )
            Spacer(
                modifier = GlanceModifier.width(10.dp)
            )
            ScheduleRowColumn(
                subject = scheduleRow.lesson,
                teacher = scheduleRow.teacher
            )
        }
    }

    @Composable
    private fun DividedScheduleRowContent(
        modifier: GlanceModifier = GlanceModifier,
        settings: WidgetSettings,
        scheduleRow: GroupScheduleRow.Divided,
        weekLabel: WeekLabel,
    ) {
        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically,
            modifier = modifier
        ) {
            Text(
                text = ResourceString.lessonNumberFormat.asGlanceString(scheduleRow.lessonNumber),
                style = TextStyle(
                    color = ColorProvider(md_theme_dark_onSurface),
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = GlanceModifier.width(10.dp))
            Column {
                if (!settings.hideLabel || weekLabel == WeekLabel.Numerator) {
                    DividedScheduleRowLabel(
                        scheduleRow = scheduleRow.numerator,
                        rowWeekLabel = WeekLabel.Numerator,
                        modifier = GlanceModifier.fillMaxWidth()
                    )
                    if (!settings.hideLabel) {
                        Spacer(modifier = GlanceModifier.height(20.dp))
                    }
                }
                if (!settings.hideLabel || weekLabel == WeekLabel.Denominator) {
                    DividedScheduleRowLabel(
                        scheduleRow = scheduleRow.denominator,
                        rowWeekLabel = WeekLabel.Denominator,
                        modifier = GlanceModifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    @Composable
    private fun DividedScheduleRowLabel(
        modifier: GlanceModifier = GlanceModifier,
        scheduleRow: GroupScheduleRow.Divided.Label,
        rowWeekLabel: WeekLabel,
    ) {
        ColoredSurface(
            color = rowWeekLabel.color(),
            modifier = modifier
        ) {
            ScheduleRowColumn(
                subject = scheduleRow.lesson,
                teacher = scheduleRow.teacher
            )
        }
    }

    @Composable
    private fun ColoredSurface(
        modifier: GlanceModifier,
        color: Color,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        Column(
            modifier = modifier
                .background(color = color)
                .cornerRadius(20.dp)
        ) {
            Column(
                content = content,
                modifier = GlanceModifier.padding(20.dp)
            )
        }
    }

    @Composable
    private fun ScheduleRowColumn(
        modifier: GlanceModifier = GlanceModifier,
        subject: String,
        teacher: String,
    ) {
        Column(modifier = modifier) {
            Text(
                text = subject,
                style = TextStyle(
                    color = ColorProvider(md_theme_dark_onSurface),
                    fontSize = 16.sp
                )
            )
            Text(
                text = teacher,
                style = TextStyle(
                    color = ColorProvider(md_theme_dark_onSurface),
                    fontSize = 14.sp
                )
            )
        }
    }
}

class ScheduleWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ScheduleWidget()
}

object ScheduleWidgetStateDefinition : GlanceStateDefinition<Preferences> {
    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        return context.scheduleWidgetDataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return File(context.applicationContext.filesDir, "datastore/$fileName")
    }

    private const val fileName = "schedule_widget_store"
    private val Context.scheduleWidgetDataStore: DataStore<Preferences>
            by preferencesDataStore(name = fileName)
}
