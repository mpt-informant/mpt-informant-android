package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.models.settings.WidgetSettings
import me.kofesst.android.mptinformant.domain.repositories.PreferencesRepository

class SaveWidgetSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(widgetSettings: WidgetSettings) =
        preferencesRepository.saveWidgetSettings(widgetSettings)
}