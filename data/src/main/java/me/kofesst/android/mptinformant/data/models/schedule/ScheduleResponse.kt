package me.kofesst.android.mptinformant.data.models.schedule

import me.kofesst.android.mptinformer.domain.models.DayOfWeek
import me.kofesst.android.mptinformer.domain.models.WeekLabel
import me.kofesst.android.mptinformer.domain.models.schedule.GroupSchedule
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleDay
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleRow

data class ScheduleResponse(
    val week_label: String,
    val group_id: String,
    val days: List<ScheduleDayDto>,
) {
    fun toSchedule() = GroupSchedule(
        weekLabel = WeekLabel.fromDisplayName(week_label),
        groupId = group_id,
        days = days.map { dayDto ->
            GroupScheduleDay(
                dayOfWeek = DayOfWeek.values()[dayDto.day_index],
                branch = dayDto.branch,
                rows = dayDto.rows.map { rowDto ->
                    with(rowDto) {
                        when (type) {
                            "single" -> GroupScheduleRow.Single(
                                lessonNumber = lesson_number,
                                lesson = lesson!!,
                                teacher = teacher!!
                            )
                            "divided" -> GroupScheduleRow.Divided(
                                lessonNumber = lesson_number,
                                numerator = GroupScheduleRow.Divided.Label(
                                    lesson = rowDto.numerator!!.lesson,
                                    teacher = rowDto.numerator.teacher
                                ),
                                denominator = GroupScheduleRow.Divided.Label(
                                    lesson = rowDto.denominator!!.lesson,
                                    teacher = rowDto.denominator.teacher
                                )
                            )
                            else -> GroupScheduleRow.Single(
                                lessonNumber = -1,
                                lesson = "Null",
                                teacher = "Null"
                            )
                        }
                    }
                }
            )
        }
    )
}