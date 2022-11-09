package me.kofesst.android.mptinformant.data.repositories

import me.kofesst.android.mptinformant.data.remote.InformantApiService
import me.kofesst.android.mptinformant.data.utils.handle
import me.kofesst.android.mptinformant.domain.models.Department
import me.kofesst.android.mptinformant.domain.repositories.DepartmentsRepository

class DepartmentsRepositoryImpl(
    private val apiService: InformantApiService,
) : DepartmentsRepository {
    override suspend fun getDepartments(): List<Department> =
        apiService.getAllDepartments().handle()?.toDepartmentsList() ?: emptyList()
}
