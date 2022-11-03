package me.kofesst.android.mptinformant.widget.worker

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import me.kofesst.android.mptinformant.data.utils.calendar
import me.kofesst.android.mptinformant.data.utils.getDayOfWeek
import me.kofesst.android.mptinformant.widget.ScheduleWidget
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleDay
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleRow
import me.kofesst.android.mptinformer.domain.models.settings.WidgetSettings
import me.kofesst.android.mptinformer.domain.usecases.UseCases
import java.util.*

@HiltWorker
class ScheduleWorkerTask @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases,
) : CoroutineWorker(context, workerParams) {
    companion object {
        val json = Json {
            useArrayPolymorphism = true
            serializersModule = SerializersModule {
                polymorphic(GroupScheduleRow::class) {
                    subclass(GroupScheduleRow.Single::class, GroupScheduleRow.Single.serializer())
                    subclass(GroupScheduleRow.Divided::class, GroupScheduleRow.Divided.serializer())
                    subclass(GroupScheduleRow.Divided.Label::class,
                        GroupScheduleRow.Divided.Label.serializer())
                }
                polymorphic(Set::class) {
                    subclass(Set::class, SetSerializer(PolymorphicSerializer(Any::class).nullable))
                }
                polymorphic(GroupScheduleDay::class) {
                    subclass(GroupScheduleDay::class, GroupScheduleDay.serializer())
                }
                polymorphic(WidgetSettings::class) {
                    subclass(WidgetSettings::class, WidgetSettings.serializer())
                }
            }
        }
    }

    override suspend fun doWork(): Result {
        val scheduleSettings = useCases.restoreScheduleSettings()
        val widgetSettings = useCases.restoreWidgetSettings()
        return try {
            updateWidget(
                context = context,
                scheduleSettings = scheduleSettings,
                widgetSettings = widgetSettings
            )
            Result.success()
        } catch (e: Exception) {
            Log.e("ScheduleWorker", e.stackTraceToString())
            Result.retry()
        }
    }

    private suspend fun updateWidget(
        context: Context,
        scheduleSettings: Pair<String, String>?,
        widgetSettings: WidgetSettings,
    ) {
        val groupId = scheduleSettings?.second
            ?: useCases.getDepartments().first().groups.first().id
        val schedule = useCases.getGroupSchedule(
            group = Group(id = groupId, name = "")
        ) ?: return
        val currentDayOfWeek = Date().calendar().getDayOfWeek()
        val showNextDay = with(
            Calendar.getInstance().apply {
                time = Date()
            }
        ) {
            val currentTime = get(Calendar.HOUR_OF_DAY) * 100 + get(Calendar.MINUTE)
            val nextDayTime = widgetSettings.nextDayHours * 100 + widgetSettings.nextDayMinutes
            currentTime >= nextDayTime
        }
        val dayToDisplay = schedule.days.firstOrNull { scheduleDay ->
            scheduleDay.dayOfWeek == currentDayOfWeek.run {
                if (showNextDay) {
                    next()
                } else {
                    this
                }
            }
        } ?: schedule.days.first()
        val scheduleJson = json.encodeToString(dayToDisplay)
        val settingsJson = json.encodeToString(widgetSettings)

        GlanceAppWidgetManager(context)
            .getGlanceIds(ScheduleWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { preferences ->
                    preferences[ScheduleWidget.schedulePreferencesKey] = scheduleJson
                    preferences[ScheduleWidget.weekLabelPreferencesKey] =
                        schedule.weekLabel.ordinal.toString()
                    preferences[ScheduleWidget.widgetSettingsPreferencesKey] = settingsJson
                }
            }
        ScheduleWidget().updateAll(context)
    }
}
