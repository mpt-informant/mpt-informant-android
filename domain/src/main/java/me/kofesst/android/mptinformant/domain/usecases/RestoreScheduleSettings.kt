package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.repositories.PreferencesRepository

class RestoreScheduleSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke() =
        preferencesRepository.restoreScheduleSettings()
}