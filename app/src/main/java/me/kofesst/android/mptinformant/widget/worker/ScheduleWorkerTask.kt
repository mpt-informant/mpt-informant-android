package me.kofesst.android.mptinformant.widget.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.models.WeekLabel
import me.kofesst.android.mptinformant.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformant.domain.models.schedule.GroupSchedule
import me.kofesst.android.mptinformant.domain.models.schedule.GroupScheduleDay
import me.kofesst.android.mptinformant.domain.models.schedule.GroupScheduleRow
import me.kofesst.android.mptinformant.domain.models.settings.AppSettings
import me.kofesst.android.mptinformant.domain.models.settings.WidgetSettings
import me.kofesst.android.mptinformant.domain.usecases.UseCases
import me.kofesst.android.mptinformant.presentation.MainActivity
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.widget.ScheduleWidget
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

    private var changesNotificationId: Int = 1

    init {
        createNotificationsChannel()
    }

    override suspend fun doWork(): Result {
        val scheduleSettings = useCases.restoreScheduleSettings()
        val widgetSettings = useCases.restoreWidgetSettings()
        val appSettings = useCases.restoreAppSettings()
        return try {
            updateWidget(
                context = context,
                scheduleSettings = scheduleSettings,
                widgetSettings = widgetSettings,
                appSettings = appSettings
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
        appSettings: AppSettings,
    ) {
        val groupId = scheduleSettings?.second
            ?: useCases.getDepartments().first().groups.first().id
        val schedule = getSchedule(groupId) ?: return
        val scheduleDay = getScheduleDayToDisplay(schedule, widgetSettings)
        val lastChanges = useCases.restoreLastGroupChanges()
        val newChanges = getChanges(groupId)
        if (appSettings.showChangesNotification) {
            checkForNewChanges(
                lastChanges = lastChanges,
                newChanges = newChanges
            )
        }
        useCases.saveLastGroupChanges(newChanges)
        GlanceAppWidgetManager(context)
            .getGlanceIds(ScheduleWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { preferences ->
                    preferences.updateWidgetState(
                        scheduleDay = scheduleDay,
                        hasChanges = hasChangesForScheduleDay(
                            changes = lastChanges,
                            scheduleDay = scheduleDay
                        ),
                        weekLabel = schedule.weekLabel,
                        widgetSettings = widgetSettings
                    )
                }
            }
        ScheduleWidget().updateAll(context)
    }

    private fun hasChangesForScheduleDay(
        changes: GroupChanges?,
        scheduleDay: GroupScheduleDay,
    ): Boolean {
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

    private fun checkForNewChanges(
        lastChanges: GroupChanges?,
        newChanges: GroupChanges?,
    ) {
        val shouldNotify =
            newChanges != null && newChanges.days.isNotEmpty() && newChanges != lastChanges
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
        val contentIntent = createChangesNotificationIntent()
        return NotificationCompat.Builder(context, CHANGES_CHANNEL_ID)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build().apply {
                flags = flags or Notification.FLAG_AUTO_CANCEL
            }
    }

    private fun createChangesNotificationIntent(): PendingIntent {
        val activityIntent = Intent(context, MainActivity::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        } else {
            PendingIntent.FLAG_CANCEL_CURRENT
        }
        return PendingIntent.getActivity(
            context,
            0,
            activityIntent,
            flags
        )
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
