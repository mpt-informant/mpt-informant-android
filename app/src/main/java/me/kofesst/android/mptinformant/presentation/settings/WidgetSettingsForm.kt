package me.kofesst.android.mptinformant.presentation.settings

import me.kofesst.android.mptinformant.domain.models.settings.WidgetSettings
import me.kofesst.android.mptinformant.ui.ResourceString

data class WidgetSettingsForm(
    val nextDayHours: Int? = 17,
    val hoursErrorMessage: ResourceString? = null,
    val nextDayMinutes: Int? = 0,
    val minutesErrorMessage: ResourceString? = null,
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
