package me.kofesst.android.mptinformant.data.models.time

import me.kofesst.android.mptinformer.domain.models.time.TimeTablePair

data class TimeTableRowDto(
    val lessonNumber: Int,
    val start_time: TimeTablePair,
    val end_time: TimeTablePair,
)
