package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class RestoreScheduleSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke() =
        preferencesRepository.restoreScheduleSettings()
}