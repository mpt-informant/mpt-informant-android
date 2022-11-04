package me.kofesst.android.mptinformant.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsForm
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsFormAction
import me.kofesst.android.mptinformer.domain.usecases.UseCases
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _widgetSettingsChannel = Channel<Boolean>()
    val widgetSettingsSubmitResult = _widgetSettingsChannel.receiveAsFlow()

    private val _widgetSettingsForm = mutableStateOf(WidgetSettingsForm())
    val widgetSettingsForm: State<WidgetSettingsForm> get() = _widgetSettingsForm

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
            WidgetSettingsFormAction.Submit -> {
                submitWidgetSettings()
            }
        }
    }

    private fun submitWidgetSettings() {
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
            saveWidgetSettings()
        }
    }

    private fun saveWidgetSettings() {
        viewModelScope.launch {
            useCases.saveWidgetSettings(widgetSettingsForm.value.toModel())
            _widgetSettingsChannel.send(true)
        }
    }
}