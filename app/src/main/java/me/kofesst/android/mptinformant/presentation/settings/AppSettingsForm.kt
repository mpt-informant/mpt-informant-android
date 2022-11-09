package me.kofesst.android.mptinformant.presentation.settings

import me.kofesst.android.mptinformant.domain.models.settings.AppSettings

data class AppSettingsForm(
    val useWeekLabelTheme: Boolean = false,
    val showChangesNotification: Boolean = true,
) {
    fun toModel() = AppSettings(
        useWeekLabelTheme = useWeekLabelTheme,
        showChangesNotification = showChangesNotification
    )
}