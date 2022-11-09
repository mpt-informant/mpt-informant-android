package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.repositories.ScheduleRepository

class GetGroupSchedule(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(group: Group) =
        scheduleRepository.getGroupSchedule(group)
}