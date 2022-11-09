package me.kofesst.android.mptinformant.domain.repositories

import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.models.changes.GroupChanges

interface ChangesRepository {
    suspend fun getGroupChanges(group: Group): GroupChanges?
}
