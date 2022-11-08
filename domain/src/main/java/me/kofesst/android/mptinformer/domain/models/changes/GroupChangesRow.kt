package me.kofesst.android.mptinformer.domain.models.changes

import kotlinx.serialization.Serializable

@Serializable
sealed class GroupChangesRow {
    abstract val lessonNumber: Int
    abstract val replacementLesson: String
    abstract val insertTimestamp: Long

    @Serializable
    data class Replace(
        override val lessonNumber: Int,
        val replacedLesson: String,
        override val replacementLesson: String,
        override val insertTimestamp: Long,
    ) : GroupChangesRow()

    @Serializable
    data class Additional(
        override val lessonNumber: Int,
        override val replacementLesson: String,
        override val insertTimestamp: Long,
    ) : GroupChangesRow()
}
