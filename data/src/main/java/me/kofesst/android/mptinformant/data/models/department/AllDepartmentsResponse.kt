package me.kofesst.android.mptinformant.data.models.department

import me.kofesst.android.mptinformant.domain.models.Department
import me.kofesst.android.mptinformant.domain.models.Group

class AllDepartmentsResponse : ArrayList<DepartmentDto>() {
    fun toDepartmentsList(): List<Department> = map { dto ->
        Department(
            id = dto.id,
            name = dto.name,
            groups = dto.groups.map { groupDto ->
                Group(
                    id = groupDto.id,
                    name = groupDto.name
                )
            }
        )
    }
}