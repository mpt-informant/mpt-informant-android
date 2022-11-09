package me.kofesst.android.mptinformant.domain.repositories

import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.models.WeekLabel
import me.kofesst.android.mptinformant.domain.models.schedule.GroupSchedule

interface ScheduleRepository {
    suspend fun getWeekLabel(): WeekLabel
    suspend fun getGroupSchedule(group: Group): GroupSchedule?
}
