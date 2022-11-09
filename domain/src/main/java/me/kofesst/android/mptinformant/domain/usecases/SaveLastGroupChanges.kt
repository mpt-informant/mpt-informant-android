package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformant.domain.repositories.PreferencesRepository

class SaveLastGroupChanges(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(changes: GroupChanges?) =
        preferencesRepository.saveLastGroupChanges(changes)
}