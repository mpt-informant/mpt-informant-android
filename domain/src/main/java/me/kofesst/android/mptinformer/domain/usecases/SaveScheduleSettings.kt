package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.models.Department
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class SaveScheduleSettings(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(department: Department, group: Group) =
        preferencesRepository.saveScheduleSettings(department, group)
}