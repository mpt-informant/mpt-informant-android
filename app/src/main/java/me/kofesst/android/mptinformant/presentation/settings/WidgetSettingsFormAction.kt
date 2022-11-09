package me.kofesst.android.mptinformant.presentation.settings

sealed class WidgetSettingsFormAction {
    data class NextDayHourChanged(val hour: Int?) : WidgetSettingsFormAction()
    data class NextDayMinuteChanged(val minute: Int?) : WidgetSettingsFormAction()
    data class HideLabelChanged(val checked: Boolean) : WidgetSettingsFormAction()
    data class ShowChangesMessageChanged(val checked: Boolean) : WidgetSettingsFormAction()
}