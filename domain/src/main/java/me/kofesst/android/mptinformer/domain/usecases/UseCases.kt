package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.repositories.ChangesRepository
import me.kofesst.android.mptinformer.domain.repositories.DepartmentsRepository
import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository
import me.kofesst.android.mptinformer.domain.repositories.ScheduleRepository

class UseCases(
    departmentsRepository: DepartmentsRepository,
    scheduleRepository: ScheduleRepository,
    changesRepository: ChangesRepository,
    preferencesRepository: PreferencesRepository,

    val getDepartments: GetDepartments = GetDepartments(departmentsRepository),
    val getGroupSchedule: GetGroupSchedule = GetGroupSchedule(scheduleRepository),
    val getGroupChanges: GetGroupChanges = GetGroupChanges(changesRepository),
    val saveScheduleSettings: SaveScheduleSettings = SaveScheduleSettings(preferencesRepository),
    val restoreScheduleSettings: RestoreScheduleSettings = RestoreScheduleSettings(
        preferencesRepository
    ),
    val saveWidgetSettings: SaveWidgetSettings = SaveWidgetSettings(preferencesRepository),
    val restoreWidgetSettings: RestoreWidgetSettings = RestoreWidgetSettings(preferencesRepository),
    val saveLastGroupChanges: SaveLastGroupChanges = SaveLastGroupChanges(preferencesRepository),
    val restoreLastGroupChanges: RestoreLastGroupChanges = RestoreLastGroupChanges(
        preferencesRepository),
)
