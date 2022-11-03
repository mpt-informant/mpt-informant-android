package me.kofesst.android.mptinformer.domain.models.settings

data class WidgetSettings(
    val nextDayHours: Int = 17,
    val nextDayMinutes: Int = 0,
    val hideLabel: Boolean = true,
    val showChangesMessage: Boolean = true,
)