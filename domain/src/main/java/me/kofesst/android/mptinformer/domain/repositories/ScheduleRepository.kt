package me.kofesst.android.mptinformer.domain.repositories

import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.schedule.GroupSchedule

interface ScheduleRepository {
    suspend fun getGroupSchedule(group: Group): GroupSchedule?
}
