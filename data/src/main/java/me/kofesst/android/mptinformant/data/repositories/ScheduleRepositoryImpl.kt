package me.kofesst.android.mptinformant.data.repositories

import me.kofesst.android.mptinformant.data.remote.InformantApiService
import me.kofesst.android.mptinformant.data.utils.handle
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.schedule.GroupSchedule
import me.kofesst.android.mptinformer.domain.repositories.ScheduleRepository

class ScheduleRepositoryImpl(
    private val apiService: InformantApiService,
) : ScheduleRepository {
    override suspend fun getGroupSchedule(group: Group): GroupSchedule? =
        apiService.getGroupSchedule(group.id).handle()?.toSchedule()
}
