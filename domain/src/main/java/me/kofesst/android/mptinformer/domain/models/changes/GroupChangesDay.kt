package me.kofesst.android.mptinformer.domain.models.changes

import me.kofesst.android.mptinformer.domain.models.DayOfWeek

data class GroupChangesDay(
    val timestamp: Long,
    val dayOfWeek: DayOfWeek,
    val rows: List<GroupChangesRow>,
)