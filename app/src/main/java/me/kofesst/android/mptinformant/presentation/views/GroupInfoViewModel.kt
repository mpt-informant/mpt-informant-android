package me.kofesst.android.mptinformant.presentation.views

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.domain.models.Department
import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformant.domain.models.schedule.GroupSchedule
import me.kofesst.android.mptinformant.domain.usecases.UseCases
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.presentation.utils.loadSuspend

@HiltViewModel
class GroupInfoViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _viewState = mutableStateOf(GroupInfoViewState())
    val viewState: State<GroupInfoViewState> = _viewState

    private val _scheduleState = mutableStateOf<SuspendValue<GroupSchedule>>(SuspendValue())
    val scheduleState: State<SuspendValue<GroupSchedule>> = _scheduleState

    private val _changesState = mutableStateOf<SuspendValue<GroupChanges>>(SuspendValue())
    val changesState: State<SuspendValue<GroupChanges>> = _changesState

    fun initializeViewState() {
        viewModelScope.launch {
            val departmentsList = useCases.getDepartments()
            val scheduleSettings = useCases.restoreScheduleSettings()

            var department: Department? = null
            var group: Group? = null
            if (scheduleSettings != null) {
                department = departmentsList.firstOrNull { it.id == scheduleSettings.first }
                group = department?.groups?.firstOrNull { it.id == scheduleSettings.second }
            }
            if (departmentsList.isEmpty()) {
                return@launch
            }
            if (department == null) {
                department = departmentsList.first()
            }
            if (group == null) {
                group = department.groups.first()
            }

            _viewState.value = GroupInfoViewState(
                departmentsList = departmentsList,
                department = department,
                group = group
            )

            loadSchedule()
            loadChanges()
        }
    }

    fun setDepartment(department: Department) {
        with(viewState.value) {
            if (this.department?.id == department.id) return

            _viewState.value = viewState.value.copy(
                department = department,
                group = department.groups.first()
            )
            saveScheduleSettings()
            refreshSchedule()
            refreshChanges()
        }
    }

    fun setGroup(group: Group) {
        with(viewState.value) {
            if (this.group?.id == group.id) return

            _viewState.value = viewState.value.copy(
                group = group
            )
            saveScheduleSettings()
            refreshSchedule()
            refreshChanges()
        }
    }

    private fun saveScheduleSettings() {
        viewModelScope.launch {
            useCases.saveScheduleSettings(
                department = viewState.value.department!!,
                group = viewState.value.group!!
            )
        }
    }

    fun refreshSchedule() {
        viewModelScope.launch {
            loadSchedule()
        }
    }

    fun refreshChanges() {
        viewModelScope.launch {
            loadChanges()
        }
    }

    private suspend fun loadSchedule() {
        val group = viewState.value.group ?: return
        _scheduleState.loadSuspend {
            useCases.getGroupSchedule(group)
        }
    }

    private suspend fun loadChanges() {
        val group = viewState.value.group ?: return
        _changesState.loadSuspend {
            useCases.getGroupChanges(group)
        }
    }
}
