package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.repositories.DepartmentsRepository

class GetDepartments(private val departmentsRepository: DepartmentsRepository) {
    suspend operator fun invoke() =
        departmentsRepository.getDepartments()
}