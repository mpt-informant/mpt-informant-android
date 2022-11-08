package me.kofesst.android.mptinformer.domain.models.changes

import kotlinx.serialization.Serializable

@Serializable
data class GroupChanges(
    val groupId: String = "",
    val days: List<GroupChangesDay> = emptyList(),
)