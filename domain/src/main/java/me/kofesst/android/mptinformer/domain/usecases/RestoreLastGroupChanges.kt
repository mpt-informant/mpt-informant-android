package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class RestoreLastGroupChanges(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke() =
        preferencesRepository.restoreLastGroupChanges()
}