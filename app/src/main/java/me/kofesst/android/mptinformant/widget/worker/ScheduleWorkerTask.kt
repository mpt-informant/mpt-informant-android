package me.kofesst.android.mptinformant.widget.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.datastore.preferences.core.MutablePreferences
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import me.kofesst.android.mptinformant.R
import me.kofesst.android.mptinformant.data.utils.calendar
import me.kofesst.android.mptinformant.data.utils.getDayOfWeek
import me.kofesst.android.mptinformant.presentation.MainActivity
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.widget.ScheduleWidget
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.WeekLabel
import me.kofesst.android.mptinformer.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformer.domain.models.schedule.GroupSchedule
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleDay
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleRow
import me.kofesst.android.mptinformer.domain.models.settings.WidgetSettings
import me.kofesst.android.mptinformer.domain.usecases.UseCases

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
                    subclass(
                        GroupScheduleRow.Divided.Label::class,
                        GroupScheduleRow.Divided.Label.serializer()
                    )
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

        const val UNIQUE_WORKER_TAG = "SCHEDULE_WORKER_TASK"
        private const val CHANGES_CHANNEL_ID = "MPT Informant Changes"
    }

    private var changes: GroupChanges? = null
    private var changesNotificationId: Int = 1

    init {
        createNotificationsChannel()
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
        val schedule = getSchedule(groupId) ?: return
        val scheduleDay = getScheduleDayToDisplay(schedule, widgetSettings)
        checkForNewChanges(getChanges(groupId))
        GlanceAppWidgetManager(context)
            .getGlanceIds(ScheduleWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { preferences ->
                    preferences.updateWidgetState(
                        scheduleDay = scheduleDay,
                        hasChanges = hasChangesForScheduleDay(scheduleDay),
                        weekLabel = schedule.weekLabel,
                        widgetSettings = widgetSettings
                    )
                }
            }
        ScheduleWidget().updateAll(context)
    }

    private fun hasChangesForScheduleDay(scheduleDay: GroupScheduleDay): Boolean {
        return changes?.days?.firstOrNull { changesDay ->
            changesDay.dayOfWeek == scheduleDay.dayOfWeek
        } != null
    }

    private suspend fun getSchedule(groupId: String): GroupSchedule? {
        return useCases.getGroupSchedule(
            group = Group(
                id = groupId,
                name = ""
            )
        )
    }

    private suspend fun getChanges(groupId: String): GroupChanges? {
        return useCases.getGroupChanges(
            group = Group(
                id = groupId,
                name = ""
            )
        )
    }

    private fun checkForNewChanges(newChanges: GroupChanges?) {
        val shouldNotify =
            newChanges != null && newChanges.days.isNotEmpty() && newChanges != changes
        changes = newChanges

        if (!shouldNotify) return

        Intent().also { intent ->
            val intentData = ResourceString.newChangesNotificationDescription.asString(context)
            intent.action = MainActivity.CHANGES_RECEIVER_ACTION
            intent.putExtra("data", intentData)
            context.sendBroadcast(intent)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(changesNotificationId++, buildChangesNotification())
        }
    }

    private fun createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = ResourceString.changes.asString(context)
            val channelDescription =
                ResourceString.changesNotificationChannelDescription.asString(context)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANGES_CHANNEL_ID, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = context.getSystemService<NotificationManager>()!!
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildChangesNotification(): Notification {
        val contentTitle = ResourceString.newChangesNotificationTitle.asString(context)
        val contentText = ResourceString.newChangesNotificationDescription.asString(context)
        return NotificationCompat.Builder(context, CHANGES_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun getScheduleDayToDisplay(
        schedule: GroupSchedule,
        widgetSettings: WidgetSettings,
    ): GroupScheduleDay {
        val showNextDay = with(
            Calendar.getInstance().apply {
                time = Date()
            }
        ) {
            val currentTime = get(Calendar.HOUR_OF_DAY) * 100 + get(Calendar.MINUTE)
            val nextDayTime = widgetSettings.nextDayHours * 100 + widgetSettings.nextDayMinutes
            currentTime >= nextDayTime
        }
        val dayOfWeek = Date().calendar().getDayOfWeek().run {
            if (showNextDay) {
                next()
            } else {
                this
            }
        }
        return schedule.days.firstOrNull { scheduleDay ->
            scheduleDay.dayOfWeek == dayOfWeek
        } ?: schedule.days.first()
    }

    private fun MutablePreferences.updateWidgetState(
        scheduleDay: GroupScheduleDay,
        hasChanges: Boolean,
        weekLabel: WeekLabel,
        widgetSettings: WidgetSettings,
    ) {
        val scheduleJson = json.encodeToString(scheduleDay)
        val settingsJson = json.encodeToString(widgetSettings)
        this[ScheduleWidget.schedulePreferencesKey] = scheduleJson
        this[ScheduleWidget.weekLabelPreferencesKey] = weekLabel.ordinal.toString()
        this[ScheduleWidget.widgetSettingsPreferencesKey] = settingsJson
        this[ScheduleWidget.hasChanges] = hasChanges
    }
}
