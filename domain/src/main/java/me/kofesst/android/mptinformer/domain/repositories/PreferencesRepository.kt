package me.kofesst.android.mptinformer.domain.repositories

import me.kofesst.android.mptinformer.domain.models.Department
import me.kofesst.android.mptinformer.domain.models.Group

interface PreferencesRepository {
    suspend fun saveScheduleSettings(department: Department, group: Group)
    suspend fun restoreScheduleSettings(): Pair<String, String>?
}
