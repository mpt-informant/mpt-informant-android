package me.kofesst.android.mptinformant.presentation.settings

import me.kofesst.android.mptinformant.domain.models.settings.WidgetSettings

data class WidgetSettingsForm(
    val nextDayHours: Int? = 17,
    val hoursErrorMessage: String? = null,
    val nextDayMinutes: Int? = 0,
    val minutesErrorMessage: String? = null,
    val hideLabel: Boolean = true,
    val showChangesMessage: Boolean = true,
) {
    fun toModel() = WidgetSettings(
        nextDayHours = requireNotNull(nextDayHours),
        nextDayMinutes = requireNotNull(nextDayMinutes),
        hideLabel = hideLabel,
        showChangesMessage = showChangesMessage
    )
}