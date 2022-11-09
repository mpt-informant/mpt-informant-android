package me.kofesst.android.mptinformer.domain.repositories

import me.kofesst.android.mptinformer.domain.models.Department
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformer.domain.models.settings.AppSettings
import me.kofesst.android.mptinformer.domain.models.settings.WidgetSettings

interface PreferencesRepository {
    suspend fun saveScheduleSettings(department: Department, group: Group)
    suspend fun restoreScheduleSettings(): Pair<String, String>?

    suspend fun saveWidgetSettings(widgetSettings: WidgetSettings)
    suspend fun restoreWidgetSettings(): WidgetSettings

    suspend fun saveAppSettings(appSettings: AppSettings)
    suspend fun restoreAppSettings(): AppSettings

    suspend fun saveLastGroupChanges(changes: GroupChanges?)
    suspend fun restoreLastGroupChanges(): GroupChanges?
}
