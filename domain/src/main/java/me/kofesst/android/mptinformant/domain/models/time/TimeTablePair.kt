package me.kofesst.android.mptinformant.domain.models.time

import kotlinx.serialization.Serializable

@Serializable
data class TimeTablePair(
    val hour: Int,
    val minute: Int,
)
