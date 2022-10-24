package me.kofesst.android.mptinformer.domain.models.schedule

sealed class GroupScheduleRow {
    abstract val lessonNumber: Int

    data class Single(
        override val lessonNumber: Int,
        val lesson: String,
        val teacher: String,
    ) : GroupScheduleRow()

    data class Divided(
        override val lessonNumber: Int,
        val numerator: Label,
        val denominator: Label,
    ) : GroupScheduleRow() {
        data class Label(
            val lesson: String,
            val teacher: String,
        ) : GroupScheduleRow() {
            override val lessonNumber: Int = -1
        }
    }
}
