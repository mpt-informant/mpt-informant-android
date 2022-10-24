package me.kofesst.android.mptinformer.domain.usecases

import me.kofesst.android.mptinformer.domain.repositories.DepartmentsRepository

class GetDepartments(private val departmentsRepository: DepartmentsRepository) {
    suspend operator fun invoke() =
        departmentsRepository.getDepartments()
}