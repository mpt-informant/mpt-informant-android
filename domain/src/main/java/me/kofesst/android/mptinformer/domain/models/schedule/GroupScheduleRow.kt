package me.kofesst.android.mptinformer.domain.models.schedule

import kotlinx.serialization.Serializable
import me.kofesst.android.mptinformer.domain.models.time.TimeTableRow

@Serializable
sealed class GroupScheduleRow {
    abstract val lessonNumber: Int
    abstract val timeTable: TimeTableRow

    @Serializable
    data class Single(
        override val lessonNumber: Int,
        override val timeTable: TimeTableRow,
        val lesson: String,
        val teacher: String,
    ) : GroupScheduleRow()

    @Serializable
    data class Divided(
        override val lessonNumber: Int,
        override val timeTable: TimeTableRow,
        val numerator: Label,
        val denominator: Label,
    ) : GroupScheduleRow() {
        @Serializable
        data class Label(
            val lesson: String,
            val teacher: String,
        ) : GroupScheduleRow() {
            override val lessonNumber: Int = -1
            override val timeTable: TimeTableRow = TimeTableRow()
        }
    }
}
