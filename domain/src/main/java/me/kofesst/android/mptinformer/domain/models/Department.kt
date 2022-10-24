package me.kofesst.android.mptinformer.domain.models

data class Department(
    val id: String,
    val name: String,
    val groups: List<Group>,
)