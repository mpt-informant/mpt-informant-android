package me.kofesst.android.mptinformant.data.models.changes

import me.kofesst.android.mptinformer.domain.models.DayOfWeek
import me.kofesst.android.mptinformer.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformer.domain.models.changes.GroupChangesDay
import me.kofesst.android.mptinformer.domain.models.changes.GroupChangesRow

data class ChangesResponse(
    val group_id: String,
    val days: List<ChangesDayDto>,
) {
    fun toChanges() = GroupChanges(
        groupId = group_id,
        days = days.map { dayDto ->
            GroupChangesDay(
                timestamp = dayDto.timestamp,
                dayOfWeek = DayOfWeek.values()[dayDto.day_index],
                rows = dayDto.rows.map { rowDto ->
                    when (rowDto.type) {
                        "additional" -> GroupChangesRow.Additional(
                            lessonNumber = rowDto.lesson_number,
                            replacementLesson = rowDto.replacement_lesson,
                            insertTimestamp = rowDto.insert_timestamp
                        )
                        "replace" -> GroupChangesRow.Replace(
                            lessonNumber = rowDto.lesson_number,
                            replacedLesson = rowDto.replaced_lesson!!,
                            replacementLesson = rowDto.replacement_lesson,
                            insertTimestamp = rowDto.insert_timestamp
                        )
                        else -> GroupChangesRow.Additional(
                            lessonNumber = -1,
                            replacementLesson = "Null",
                            insertTimestamp = 0
                        )
                    }
                }
            )
        }
    )
}