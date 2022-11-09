package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.models.settings.AppSettings
import me.kofesst.android.mptinformant.domain.repositories.PreferencesRepository

class SaveAppSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(appSettings: AppSettings) =
        preferencesRepository.saveAppSettings(appSettings)
}