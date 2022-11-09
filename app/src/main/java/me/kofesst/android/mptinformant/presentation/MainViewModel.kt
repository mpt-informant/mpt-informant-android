package me.kofesst.android.mptinformant.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.presentation.settings.AppSettingsForm
import me.kofesst.android.mptinformant.presentation.settings.AppSettingsFormAction
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsForm
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsFormAction
import me.kofesst.android.mptinformer.domain.usecases.UseCases
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _settingsChannel = Channel<Boolean>()
    val settingsSubmitResult = _settingsChannel.receiveAsFlow()

    private val _widgetSettingsForm = mutableStateOf(WidgetSettingsForm())
    val widgetSettingsForm: State<WidgetSettingsForm> get() = _widgetSettingsForm

    private val _appSettingsForm = mutableStateOf(AppSettingsForm())
    val appSettingsForm: State<AppSettingsForm> get() = _appSettingsForm

    fun loadSettings() {
        viewModelScope.launch {
            _widgetSettingsForm.value = with(useCases.restoreWidgetSettings()) {
                widgetSettingsForm.value.copy(
                    nextDayHours = nextDayHours,
                    nextDayMinutes = nextDayMinutes,
                    hideLabel = hideLabel,
                    showChangesMessage = showChangesMessage
                )
            }

            _appSettingsForm.value = with(useCases.restoreAppSettings()) {
                appSettingsForm.value.copy(
                    useWeekLabelTheme = useWeekLabelTheme
                )
            }
        }
    }

    fun onWidgetSettingsFormAction(action: WidgetSettingsFormAction) {
        when (action) {
            is WidgetSettingsFormAction.NextDayHourChanged -> {
                _widgetSettingsForm.value = widgetSettingsForm.value.copy(
                    nextDayHours = action.hour
                )
            }
            is WidgetSettingsFormAction.NextDayMinuteChanged -> {
                _widgetSettingsForm.value = widgetSettingsForm.value.copy(
                    nextDayMinutes = action.minute
                )
            }
            is WidgetSettingsFormAction.HideLabelChanged -> {
                _widgetSettingsForm.value = widgetSettingsForm.value.copy(
                    hideLabel = action.checked
                )
            }
            is WidgetSettingsFormAction.ShowChangesMessageChanged -> {
                _widgetSettingsForm.value = widgetSettingsForm.value.copy(
                    showChangesMessage = action.checked
                )
            }
        }
    }

    fun onAppSettingsFormAction(action: AppSettingsFormAction) {
        when (action) {
            is AppSettingsFormAction.UseWeekLabelThemeChanged -> {
                _appSettingsForm.value = appSettingsForm.value.copy(
                    useWeekLabelTheme = action.checked
                )
            }
            is AppSettingsFormAction.ShowChangesNotificationChanged -> {
                _appSettingsForm.value = appSettingsForm.value.copy(
                    showChangesNotification = action.checked
                )
            }
        }
    }

    fun submitSettings() {
        val settingsForm = widgetSettingsForm.value
        val hoursErrorMessage = when {
            settingsForm.nextDayHours == null -> "Это обязательное поле"
            settingsForm.nextDayHours < 17 -> "Минимум 17 часов"
            settingsForm.nextDayHours > 23 -> "Максимум 23 часа"
            else -> null
        }
        val minutesErrorMessage = when {
            settingsForm.nextDayMinutes == null -> "Это обязательное поле"
            settingsForm.nextDayMinutes < 0 -> "Минимум 0 минут"
            settingsForm.nextDayMinutes > 59 -> "Максимум 59 минут"
            else -> null
        }
        _widgetSettingsForm.value = settingsForm.copy(
            hoursErrorMessage = hoursErrorMessage,
            minutesErrorMessage = minutesErrorMessage
        )
        if (hoursErrorMessage == null && minutesErrorMessage == null) {
            saveSettings()
        }
    }

    private fun saveSettings() {
        viewModelScope.launch {
            useCases.saveWidgetSettings(widgetSettingsForm.value.toModel())
            useCases.saveAppSettings(appSettingsForm.value.toModel())
            _settingsChannel.send(true)
        }
    }
}