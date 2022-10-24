package me.kofesst.android.mptinformer.domain.models.schedule

import me.kofesst.android.mptinformer.domain.models.DayOfWeek

data class GroupScheduleDay(
    val dayOfWeek: DayOfWeek = DayOfWeek.Sunday,
    val branch: String,
    val rows: List<GroupScheduleRow>,
)