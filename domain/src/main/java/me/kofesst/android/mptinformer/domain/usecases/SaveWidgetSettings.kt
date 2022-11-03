package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.models.settings.WidgetSettings
import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class SaveWidgetSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(widgetSettings: WidgetSettings) =
        preferencesRepository.saveWidgetSettings(widgetSettings)
}