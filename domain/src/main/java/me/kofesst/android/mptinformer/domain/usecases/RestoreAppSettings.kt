package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class RestoreAppSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke() =
        preferencesRepository.restoreAppSettings()
}