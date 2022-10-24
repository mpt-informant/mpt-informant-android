package me.kofesst.android.mptinformer.domain.models.changes

data class GroupChanges(
    val groupId: String = "",
    val days: List<GroupChangesDay> = emptyList(),
)