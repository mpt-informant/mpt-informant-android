package me.kofesst.android.mptinformant.domain.models.changes

import kotlinx.serialization.Serializable
import me.kofesst.android.mptinformant.domain.models.DayOfWeek

@Serializable
data class GroupChangesDay(
    val timestamp: Long,
    val dayOfWeek: DayOfWeek,
    val rows: List<GroupChangesRow>,
)