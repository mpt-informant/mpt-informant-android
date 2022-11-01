package me.kofesst.android.mptinformant.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.*
import me.kofesst.android.mptinformant.R
import me.kofesst.android.mptinformant.presentation.views.GroupInfoViewTab
import me.kofesst.android.mptinformer.domain.models.DayOfWeek
import me.kofesst.android.mptinformer.domain.models.WeekLabel

class ResourceString private constructor(@StringRes private val resId: Int) {
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
    }

    @Composable
    fun asString(vararg formats: Any): String {
        return stringResource(resId, *formats)
    }
}

@Composable
fun WeekLabel.uiText(): String = (
    when (this) {
        WeekLabel.Numerator -> ResourceString.numeratorLabel
        WeekLabel.Denominator -> ResourceString.denominatorLabel
        WeekLabel.None -> ResourceString.appName
    }
    ).asString()

@Composable
fun DayOfWeek.uiText(): String = (
    when (this) {
        DayOfWeek.Sunday -> ResourceString.sunday
        DayOfWeek.Monday -> ResourceString.monday
        DayOfWeek.Tuesday -> ResourceString.tuesday
        DayOfWeek.Wednesday -> ResourceString.wednesday
        DayOfWeek.Thursday -> ResourceString.thursday
        DayOfWeek.Friday -> ResourceString.friday
        DayOfWeek.Saturday -> ResourceString.saturday
    }
    ).asString()

@Composable
fun GroupInfoViewTab.uiText(): String = (
    when (this) {
        GroupInfoViewTab.Schedule -> ResourceString.schedule
        GroupInfoViewTab.Changes -> ResourceString.changes
    }
    ).asString()

@Composable
fun Long.dateUiText(): String = with(
    SimpleDateFormat(
        ResourceString.dateFormat.asString(),
        Locale.ROOT
    )
) {
    format(Date(this@dateUiText))
}
