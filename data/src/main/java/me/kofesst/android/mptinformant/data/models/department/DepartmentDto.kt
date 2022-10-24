package me.kofesst.android.mptinformant.data.models.department

data class DepartmentDto(
    val id: String,
    val name: String,
    val groups: List<GroupDto>,
)