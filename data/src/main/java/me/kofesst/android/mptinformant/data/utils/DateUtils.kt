package me.kofesst.android.mptinformant.data.utils

import me.kofesst.android.mptinformer.domain.models.DayOfWeek
import java.util.*

fun Date.calendar(): Calendar {
    return Calendar.getInstance().apply {
        time = this@calendar
    }
}

fun Calendar.getDayOfWeek(): DayOfWeek {
    val dayOfWeekIndex = this.get(Calendar.DAY_OF_WEEK)
    return DayOfWeek.values()[dayOfWeekIndex - 1]
}