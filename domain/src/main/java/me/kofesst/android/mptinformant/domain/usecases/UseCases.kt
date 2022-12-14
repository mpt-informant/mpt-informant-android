package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.repositories.*

class UseCases(
    departmentsRepository: DepartmentsRepository,
    scheduleRepository: ScheduleRepository,
    changesRepository: ChangesRepository,
    preferencesRepository: PreferencesRepository,
    gitHubRepository: GitHubRepository,

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
    val saveAppSettings: SaveAppSettings = SaveAppSettings(preferencesRepository),
    val restoreAppSettings: RestoreAppSettings = RestoreAppSettings(preferencesRepository),
    val getWeekLabel: GetWeekLabel = GetWeekLabel(scheduleRepository),
    val getAppReleases: GetAppReleases = GetAppReleases(gitHubRepository),
)
