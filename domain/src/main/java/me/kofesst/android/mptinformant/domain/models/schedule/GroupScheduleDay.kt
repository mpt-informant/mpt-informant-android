package me.kofesst.android.mptinformant.domain.models.schedule

import kotlinx.serialization.Serializable
import me.kofesst.android.mptinformant.domain.models.DayOfWeek

@Serializable
data class GroupScheduleDay(
    val dayOfWeek: DayOfWeek = DayOfWeek.Sunday,
    val branch: String,
    val rows: List<GroupScheduleRow>,
)