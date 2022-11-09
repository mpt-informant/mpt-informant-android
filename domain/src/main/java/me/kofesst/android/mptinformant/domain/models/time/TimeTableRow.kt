package me.kofesst.android.mptinformant.domain.models.time

import kotlinx.serialization.Serializable

@Serializable
data class TimeTableRow(
    val lessonNumber: Int = 0,
    val startTime: TimeTablePair = TimeTablePair(0, 0),
    val endTime: TimeTablePair = TimeTablePair(0, 0),
)
