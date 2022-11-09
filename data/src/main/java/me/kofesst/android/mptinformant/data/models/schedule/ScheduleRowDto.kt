package me.kofesst.android.mptinformant.data.models.schedule

import me.kofesst.android.mptinformant.data.models.time.TimeTableRowDto

data class ScheduleRowDto(
    val type: String,
    val lesson_number: Int,
    val lesson: String?,
    val teacher: String?,
    val denominator: ScheduleRowLabelDto?,
    val numerator: ScheduleRowLabelDto?,
    val time_table: TimeTableRowDto,
)