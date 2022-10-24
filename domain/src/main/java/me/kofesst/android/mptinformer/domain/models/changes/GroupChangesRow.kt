package me.kofesst.android.mptinformer.domain.models.changes

sealed class GroupChangesRow {
    abstract val lessonNumber: Int
    abstract val replacementLesson: String
    abstract val insertTimestamp: Long

    data class Replace(
        override val lessonNumber: Int,
        val replacedLesson: String,
        override val replacementLesson: String,
        override val insertTimestamp: Long,
    ) : GroupChangesRow()

    data class Additional(
        override val lessonNumber: Int,
        override val replacementLesson: String,
        override val insertTimestamp: Long,
    ) : GroupChangesRow()
}
