package me.kofesst.android.mptinformant.presentation.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsForm
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsFormAction
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.components.OutlinedNumericTextField

@Composable
fun WidgetSettingsColumn(
    modifier: Modifier = Modifier,
    widgetSettingsForm: WidgetSettingsForm,
    onFormAction: (WidgetSettingsFormAction) -> Unit,
) {
    SettingsColumn(modifier = modifier) {
        SettingsColumnHeader(
            title = ResourceString.widgetSettings.asString(),
            subtitle = ResourceString.widgetSettingsDescription.asString()
        )
        Divider()
        ScheduleWidgetTimeSettings(
            hours = widgetSettingsForm.nextDayHours,
            hoursErrorMessage = widgetSettingsForm.hoursErrorMessage,
            onHoursChange = {
                onFormAction(
                    WidgetSettingsFormAction.NextDayHourChanged(it)
                )
            },
            minutes = widgetSettingsForm.nextDayMinutes,
            minutesErrorMessage = widgetSettingsForm.minutesErrorMessage,
            onMinutesChange = {
                onFormAction(
                    WidgetSettingsFormAction.NextDayMinuteChanged(it)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Divider()
        ScheduleWidgetLabelSettings(
            checked = widgetSettingsForm.hideLabel,
            onCheckedChange = {
                onFormAction(
                    WidgetSettingsFormAction.HideLabelChanged(it)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Divider()
        ScheduleWidgetChangesSettings(
            checked = widgetSettingsForm.showChangesMessage,
            onCheckedChange = {
                onFormAction(
                    WidgetSettingsFormAction.ShowChangesMessageChanged(it)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ScheduleWidgetTimeSettings(
    modifier: Modifier = Modifier,
    hours: Int?,
    hoursErrorMessage: String?,
    onHoursChange: (Int?) -> Unit,
    minutes: Int?,
    minutesErrorMessage: String?,
    onMinutesChange: (Int?) -> Unit,
) {
    SettingsFieldColumn(
        title = ResourceString.widgetTimeSettingsDescription.asString(),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedNumericTextField(
                value = hours,
                onValueChange = onHoursChange,
                onFocusLeftValueFormatter = { it.padStart(2, '0') },
                label = ResourceString.hours.asString(),
                errorMessage = hoursErrorMessage,
                modifier = Modifier.weight(1.0f)
            )
            OutlinedNumericTextField(
                value = minutes,
                onValueChange = onMinutesChange,
                onFocusLeftValueFormatter = { it.padStart(2, '0') },
                label = ResourceString.minutes.asString(),
                errorMessage = minutesErrorMessage,
                modifier = Modifier.weight(1.0f)
            )
        }
    }
}

@Composable
private fun ScheduleWidgetLabelSettings(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsFieldColumn(
        title = ResourceString.widgetLabelSettingsDescription.asString(),
        modifier = modifier
    ) {
        CheckBoxSettingsField(
            text = ResourceString.widgetHideLabelSettings.asString(),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ScheduleWidgetChangesSettings(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsFieldColumn(
        title = ResourceString.widgetChangesSettingsDescription.asString(),
        modifier = modifier
    ) {
        CheckBoxSettingsField(
            text = ResourceString.widgetShowChangesMessageSettings.asString(),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
