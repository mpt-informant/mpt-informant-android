package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.repositories.ChangesRepository

class GetGroupChanges(private val changesRepository: ChangesRepository) {
    suspend operator fun invoke(group: Group) =
        changesRepository.getGroupChanges(group)
}