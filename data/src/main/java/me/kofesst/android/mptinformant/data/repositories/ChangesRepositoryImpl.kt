package me.kofesst.android.mptinformant.data.repositories

import me.kofesst.android.mptinformant.data.remote.InformantApiService
import me.kofesst.android.mptinformant.data.utils.handle
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformer.domain.repositories.ChangesRepository

class ChangesRepositoryImpl(
    private val apiService: InformantApiService,
) : ChangesRepository {
    override suspend fun getGroupChanges(group: Group): GroupChanges? =
        apiService.getGroupChanges(group.id).handle()?.toChanges()
}
