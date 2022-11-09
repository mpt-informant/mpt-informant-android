package me.kofesst.android.mptinformant.domain.models.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val useWeekLabelTheme: Boolean = false,
    val showChangesNotification: Boolean = true,
)
