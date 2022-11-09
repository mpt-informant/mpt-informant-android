package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.repositories.ScheduleRepository

class GetWeekLabel(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke() =
        scheduleRepository.getWeekLabel()
}