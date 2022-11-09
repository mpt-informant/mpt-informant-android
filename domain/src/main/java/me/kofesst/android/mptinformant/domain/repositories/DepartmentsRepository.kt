package me.kofesst.android.mptinformant.domain.repositories

import me.kofesst.android.mptinformant.domain.models.Department

interface DepartmentsRepository {
    suspend fun getDepartments(): List<Department>
}
