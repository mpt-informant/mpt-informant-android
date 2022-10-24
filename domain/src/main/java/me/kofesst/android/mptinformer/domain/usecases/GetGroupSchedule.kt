package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.repositories.ScheduleRepository

class GetGroupSchedule(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(group: Group) =
        scheduleRepository.getGroupSchedule(group)
}