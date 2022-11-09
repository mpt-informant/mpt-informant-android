package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.models.settings.AppSettings
import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class SaveAppSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(appSettings: AppSettings) =
        preferencesRepository.saveAppSettings(appSettings)
}