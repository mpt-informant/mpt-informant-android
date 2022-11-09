package me.kofesst.android.mptinformant.domain.models.schedule

import me.kofesst.android.mptinformant.domain.models.WeekLabel

data class GroupSchedule constructor(
    val weekLabel: WeekLabel,
    val groupId: String,
    val days: List<GroupScheduleDay>,
)
