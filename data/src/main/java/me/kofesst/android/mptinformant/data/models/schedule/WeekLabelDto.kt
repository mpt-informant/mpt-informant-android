package me.kofesst.android.mptinformant.data.models.schedule

import me.kofesst.android.mptinformant.domain.models.WeekLabel

data class WeekLabelDto(
    val name: String,
    val display_name: String,
) {
    fun toModel() = WeekLabel.fromDisplayName(display_name)
}