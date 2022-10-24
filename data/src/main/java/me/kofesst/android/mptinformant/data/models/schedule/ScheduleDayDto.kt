package me.kofesst.android.mptinformant.data.models.schedule

data class ScheduleDayDto(
    val day_index: Int,
    val day_name: String,
    val branch: String,
    val rows: List<ScheduleRowDto>
)