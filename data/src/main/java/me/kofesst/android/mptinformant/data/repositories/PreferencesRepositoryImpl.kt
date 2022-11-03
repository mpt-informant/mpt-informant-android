package me.kofesst.android.mptinformant.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import me.kofesst.android.mptinformer.domain.models.Department
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.settings.WidgetSettings
import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class PreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : PreferencesRepository {
    companion object {
        private val departmentIdKey = stringPreferencesKey("department_id")
        private val groupIdKey = stringPreferencesKey("group_id")

        private val widgetSettingsNextDayHoursKey =
            intPreferencesKey("widget_next_day_hours")
        private val widgetSettingsNextDayMinutesKey =
            intPreferencesKey("widget_next_day_minutes")
        private val widgetSettingsHideLabelKey =
            booleanPreferencesKey("widget_hide_label")
        private val widgetSettingsShowChangesMessageKey =
            booleanPreferencesKey("widget_show_changes_message")
    }

    override suspend fun saveScheduleSettings(department: Department, group: Group) {
        dataStore.edit { preferences ->
            preferences[departmentIdKey] = department.id
            preferences[groupIdKey] = group.id
        }
    }

    override suspend fun restoreScheduleSettings(): Pair<String, String>? =
        dataStore.data.map { preferences ->
            val departmentId = preferences[departmentIdKey] ?: return@map null
            val groupId = preferences[groupIdKey] ?: return@map null
            departmentId to groupId
        }.firstOrNull()

    override suspend fun saveWidgetSettings(widgetSettings: WidgetSettings) {
        dataStore.edit { preferences ->
            preferences[widgetSettingsNextDayHoursKey] = widgetSettings.nextDayHours
            preferences[widgetSettingsNextDayMinutesKey] = widgetSettings.nextDayMinutes
            preferences[widgetSettingsHideLabelKey] = widgetSettings.hideLabel
            preferences[widgetSettingsShowChangesMessageKey] = widgetSettings.showChangesMessage
        }
    }

    override suspend fun restoreWidgetSettings(): WidgetSettings =
        dataStore.data.map { preferences ->
            WidgetSettings(
                nextDayHours = preferences[widgetSettingsNextDayHoursKey] ?: 17,
                nextDayMinutes = preferences[widgetSettingsNextDayMinutesKey] ?: 0,
                hideLabel = preferences[widgetSettingsHideLabelKey] ?: true,
                showChangesMessage = preferences[widgetSettingsShowChangesMessageKey] ?: true
            )
        }.firstOrNull() ?: WidgetSettings()
}
