package me.kofesst.android.mptinformer.domain.repositories

import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.changes.GroupChanges

interface ChangesRepository {
    suspend fun getGroupChanges(group: Group): GroupChanges?
}
