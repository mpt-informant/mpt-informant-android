package me.kofesst.android.mptinformer.domain.models.schedule

import kotlinx.serialization.Serializable
import me.kofesst.android.mptinformer.domain.models.DayOfWeek

@Serializable
data class GroupScheduleDay(
    val dayOfWeek: DayOfWeek = DayOfWeek.Sunday,
    val branch: String,
    val rows: List<GroupScheduleRow>,
)