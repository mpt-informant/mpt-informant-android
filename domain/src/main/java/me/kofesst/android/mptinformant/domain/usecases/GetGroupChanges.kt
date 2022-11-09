package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.repositories.ChangesRepository

class GetGroupChanges(private val changesRepository: ChangesRepository) {
    suspend operator fun invoke(group: Group) =
        changesRepository.getGroupChanges(group)
}