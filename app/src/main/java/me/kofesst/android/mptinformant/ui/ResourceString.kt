package me.kofesst.android.mptinformant.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.glance.LocalContext
import me.kofesst.android.mptinformant.R
import me.kofesst.android.mptinformant.presentation.views.GroupInfoViewTab
import me.kofesst.android.mptinformer.domain.models.DayOfWeek
import me.kofesst.android.mptinformer.domain.models.WeekLabel
import java.text.SimpleDateFormat
import java.util.*

class ResourceString private constructor(@StringRes val resId: Int) {
    companion object {
        val appName = ResourceString(R.string.app_name)
        val additionalLesson = ResourceString(R.string.additional_lesson)
        val lessonNumberFormat = ResourceString(R.string.lesson_number_format)
        val numeratorLabel = ResourceString(R.string.numerator_label)
        val denominatorLabel = ResourceString(R.string.denominator_label)
        val sunday = ResourceString(R.string.sunday)
        val monday = ResourceString(R.string.monday)
        val tuesday = ResourceString(R.string.tuesday)
        val wednesday = ResourceString(R.string.wednesday)
        val thursday = ResourceString(R.string.thursday)
        val friday = ResourceString(R.string.friday)
        val saturday = ResourceString(R.string.saturday)
        val schedule = ResourceString(R.string.schedule)
        val changes = ResourceString(R.string.changes)
        val emptySchedule = ResourceString(R.string.empty_schedule)
        val emptyChanges = ResourceString(R.string.empty_changes)
        val unexpectedError = ResourceString(R.string.unexpected_error)
        val dateFormat = ResourceString(R.string.date_format)
        val department = ResourceString(R.string.department)
        val group = ResourceString(R.string.group)
        val extraLinks = ResourceString(R.string.extra_links)
        val examsSchedule = ResourceString(R.string.exams_schedule)
        val orderCertificate = ResourceString(R.string.order_certificate)
        val author = ResourceString(R.string.author)
        val github = ResourceString(R.string.github)
        val departmentsSite = ResourceString(R.string.departments_site)
        val scheduleWidgetUpdatesSoon = ResourceString(R.string.schedule_widget_updates_soon)
        val widgetSettings = ResourceString(R.string.widget_settings)
        val widgetSettingsDescription = ResourceString(R.string.widget_settings_description)
        val widgetTimeSettingsDescription =
            ResourceString(R.string.widget_time_settings_description)
        val hours = ResourceString(R.string.hours)
        val minutes = ResourceString(R.string.minutes)
        val widgetLabelSettingsDescription =
            ResourceString(R.string.widget_label_settings_description)
        val widgetHideLabelSettings = ResourceString(R.string.widget_hide_label_settings)
        val widgetChangesSettingsDescription =
            ResourceString(R.string.widget_changes_settings_description)
        val widgetShowChangesMessageSettings =
            ResourceString(R.string.widget_show_changes_message_settings)
        val widgetSettingsSaved = ResourceString(R.string.widget_settings_saved)
        val changesNotificationChannelDescription =
            ResourceString(R.string.changes_notification_channel_description)
        val newChangesNotificationTitle = ResourceString(R.string.new_changes_notification_title)
        val newChangesNotificationDescription =
            ResourceString(R.string.new_changes_notification_description)
        val hasChangesInDay = ResourceString(R.string.has_changes_in_day)
        val saveChanges = ResourceString(R.string.save_changes)
    }

    @Composable
    fun asString(vararg formats: Any): String {
        return stringResource(resId, *formats)
    }

    fun asString(context: Context, vararg formats: Any): String {
        return context.getString(resId, *formats)
    }

    @Composable
    fun asGlanceString(vararg formats: Any): String {
        return LocalContext.current.getString(resId, *formats)
    }
}

fun WeekLabel.uiText(): ResourceString = when (this) {
    WeekLabel.Numerator -> ResourceString.numeratorLabel
    WeekLabel.Denominator -> ResourceString.denominatorLabel
    WeekLabel.None -> ResourceString.appName
}

fun DayOfWeek.uiText(): ResourceString = when (this) {
    DayOfWeek.Sunday -> ResourceString.sunday
    DayOfWeek.Monday -> ResourceString.monday
    DayOfWeek.Tuesday -> ResourceString.tuesday
    DayOfWeek.Wednesday -> ResourceString.wednesday
    DayOfWeek.Thursday -> ResourceString.thursday
    DayOfWeek.Friday -> ResourceString.friday
    DayOfWeek.Saturday -> ResourceString.saturday
}

fun GroupInfoViewTab.uiText(): ResourceString = when (this) {
    GroupInfoViewTab.Schedule -> ResourceString.schedule
    GroupInfoViewTab.Changes -> ResourceString.changes
}

@Composable
fun Long.dateUiText(): String = with(
    SimpleDateFormat(
        ResourceString.dateFormat.asString(),
        Locale.ROOT
    )
) {
    format(Date(this@dateUiText))
}
