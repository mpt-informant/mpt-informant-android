package me.kofesst.android.mptinformant.presentation.sheet

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.kofesst.android.mptinformant.presentation.settings.AppSettingsForm
import me.kofesst.android.mptinformant.presentation.settings.AppSettingsFormAction
import me.kofesst.android.mptinformant.ui.ResourceString

@Composable
fun AppSettingsColumn(
    modifier: Modifier = Modifier,
    appSettingsForm: AppSettingsForm,
    onFormAction: (AppSettingsFormAction) -> Unit,
) {
    SettingsColumn(modifier = modifier) {
        SettingsColumnHeader(title = ResourceString.appSettings.asString())
        Divider()
        UseWeekLabelThemeSettings(
            checked = appSettingsForm.useWeekLabelTheme,
            onCheckedChange = {
                onFormAction(
                    AppSettingsFormAction.UseWeekLabelThemeChanged(it)
                )
            }
        )
        Divider()
        ShowChangesNotificationSettings(
            checked = appSettingsForm.showChangesNotification,
            onCheckedChange = {
                onFormAction(
                    AppSettingsFormAction.ShowChangesNotificationChanged(it)
                )
            }
        )
    }
}

@Composable
private fun UseWeekLabelThemeSettings(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsFieldColumn(
        title = ResourceString.useWeekLabelThemeSettingsDescription.asString(),
        modifier = modifier
    ) {
        CheckBoxSettingsField(
            text = ResourceString.useWeekLabelThemeSettingsMessage.asString(),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ShowChangesNotificationSettings(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsFieldColumn(
        title = ResourceString.showChangesNotificationSettingsDescription.asString(),
        modifier = modifier
    ) {
        CheckBoxSettingsField(
            text = ResourceString.showChangesNotificationSettingsMessage.asString(),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}