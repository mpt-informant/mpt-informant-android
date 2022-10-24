package me.kofesst.android.mptinformant.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import me.kofesst.android.mptinformer.domain.models.Department
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository

class PreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : PreferencesRepository {
    companion object {
        private val departmentIdKey = stringPreferencesKey("department_id")
        private val groupIdKey = stringPreferencesKey("group_id")
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
}
