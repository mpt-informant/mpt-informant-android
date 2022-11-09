package me.kofesst.android.mptinformant.presentation.settings

sealed class AppSettingsFormAction {
    data class UseWeekLabelThemeChanged(val checked: Boolean) : AppSettingsFormAction()
    data class ShowChangesNotificationChanged(val checked: Boolean) : AppSettingsFormAction()
}