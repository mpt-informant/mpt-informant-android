package me.kofesst.android.mptinformant.presentation.settings

import me.kofesst.android.mptinformer.domain.models.settings.AppSettings

data class AppSettingsForm(
    val useWeekLabelTheme: Boolean = false,
    val showChangesNotification: Boolean = true,
) {
    fun toModel() = AppSettings(
        useWeekLabelTheme = useWeekLabelTheme,
        showChangesNotification = showChangesNotification
    )
}