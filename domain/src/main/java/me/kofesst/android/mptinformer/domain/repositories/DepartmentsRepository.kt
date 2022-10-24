package me.kofesst.android.mptinformer.domain.repositories

import me.kofesst.android.mptinformer.domain.models.Department

interface DepartmentsRepository {
    suspend fun getDepartments(): List<Department>
}
