package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class SaveLastGroupChanges(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(changes: GroupChanges?) =
        preferencesRepository.saveLastGroupChanges(changes)
}