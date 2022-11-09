package me.kofesst.android.mptinformant.domain.repositories

import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.models.schedule.GroupSchedule

interface ScheduleRepository {
    suspend fun getGroupSchedule(group: Group): GroupSchedule?
}
