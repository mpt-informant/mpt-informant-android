package me.kofesst.android.mptinformer.domain.models

enum class DayOfWeek {
    Sunday,
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday;

    fun next() = if (ordinal == Saturday.ordinal) {
        Sunday
    } else {
        values()[ordinal + 1]
    }
}