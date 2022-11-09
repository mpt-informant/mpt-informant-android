package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.models.Department
import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.repositories.PreferencesRepository

class SaveScheduleSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(department: Department, group: Group) =
        preferencesRepository.saveScheduleSettings(department, group)
}