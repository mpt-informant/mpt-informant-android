package me.kofesst.android.mptinformer.domain.models.schedule

import me.kofesst.android.mptinformer.domain.models.WeekLabel

data class GroupSchedule constructor(
    val weekLabel: WeekLabel,
    val groupId: String,
    val days: List<GroupScheduleDay>,
)
