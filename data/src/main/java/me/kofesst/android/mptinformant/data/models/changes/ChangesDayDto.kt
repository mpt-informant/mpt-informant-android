package me.kofesst.android.mptinformant.data.models.changes

data class ChangesDayDto(
    val day_index: Int,
    val day_name: String,
    val timestamp: Long,
    val rows: List<ChangesRowDto>,
)