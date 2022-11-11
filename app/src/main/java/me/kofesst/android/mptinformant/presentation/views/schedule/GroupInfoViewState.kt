package me.kofesst.android.mptinformant.presentation.views.schedule

import me.kofesst.android.mptinformant.domain.models.Department
import me.kofesst.android.mptinformant.domain.models.Group

data class GroupInfoViewState(
    val departmentsList: List<Department> = emptyList(),
    val department: Department? = null,
    val group: Group? = null,
) {
    val isValid: Boolean
        get() = department != null && group != null
}
