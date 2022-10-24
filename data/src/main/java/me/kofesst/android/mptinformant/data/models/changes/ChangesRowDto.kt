package me.kofesst.android.mptinformant.data.models.changes

data class ChangesRowDto(
    val type: String,
    val lesson_number: Int,
    val replaced_lesson: String?,
    val replacement_lesson: String,
    val insert_timestamp: Long,
)