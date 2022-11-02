package me.kofesst.android.mptinformer.domain.models.schedule

import kotlinx.serialization.Serializable

@Serializable
sealed class GroupScheduleRow {
    abstract val lessonNumber: Int

    @Serializable
    data class Single(
        override val lessonNumber: Int,
        val lesson: String,
        val teacher: String,
    ) : GroupScheduleRow()

    @Serializable
    data class Divided(
        override val lessonNumber: Int,
        val numerator: Label,
        val denominator: Label,
    ) : GroupScheduleRow() {
        @Serializable
        data class Label(
            val lesson: String,
            val teacher: String,
        ) : GroupScheduleRow() {
            override val lessonNumber: Int = -1
        }
    }
}
