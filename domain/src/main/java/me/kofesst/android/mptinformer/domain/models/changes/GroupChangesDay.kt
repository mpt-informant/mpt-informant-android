package me.kofesst.android.mptinformer.domain.models.changes

import kotlinx.serialization.Serializable
import me.kofesst.android.mptinformer.domain.models.DayOfWeek

@Serializable
data class GroupChangesDay(
    val timestamp: Long,
    val dayOfWeek: DayOfWeek,
    val rows: List<GroupChangesRow>,
)