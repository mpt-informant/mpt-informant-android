package me.kofesst.android.mptinformant.domain.models.settings

import kotlinx.serialization.Serializable

@Serializable
data class WidgetSettings(
    val nextDayHours: Int = 17,
    val nextDayMinutes: Int = 0,
    val hideLabel: Boolean = true,
    val showChangesMessage: Boolean = true,
)