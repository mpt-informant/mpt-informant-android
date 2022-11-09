package me.kofesst.android.mptinformant.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import me.kofesst.android.mptinformant.ui.ResourceString

sealed class ExtraLink(
    val href: String,
    val text: ResourceString,
    val icon: ImageVector,
) {
    companion object {
        fun default() = listOf(
            Schedule, Changes, Departments,
            OrderCertificate, ExamsSchedule
        )

        fun additional() = listOf(
            Author, GitHub
        )
    }

    object Schedule : ExtraLink(
        href = "https://mpt.ru/studentu/raspisanie-zanyatiy/",
        text = ResourceString.schedule,
        icon = Icons.Outlined.CalendarToday
    )

    object Changes : ExtraLink(
        href = "https://mpt.ru/studentu/izmeneniya-v-raspisanii/",
        text = ResourceString.changes,
        icon = Icons.Outlined.EditCalendar
    )

    object Departments : ExtraLink(
        href = "https://mpt.ru/sites-otdels/",
        text = ResourceString.departmentsSite,
        icon = Icons.Outlined.Groups
    )

    object OrderCertificate : ExtraLink(
        href = "https://student.mpt.ru/",
        text = ResourceString.orderCertificate,
        icon = Icons.Outlined.Assignment
    )

    object ExamsSchedule : ExtraLink(
        href = "https://mpt.ru/studentu/raspisanie-ekzamenov/",
        text = ResourceString.examsSchedule,
        icon = Icons.Outlined.ReceiptLong
    )

    object Author : ExtraLink(
        href = "https://vk.com/kofesst",
        text = ResourceString.author,
        icon = Icons.Outlined.Person
    )

    object GitHub : ExtraLink(
        href = "https://github.com/mpt-informant",
        text = ResourceString.github,
        icon = Icons.Outlined.DeviceHub
    )

    object Settings : ExtraLink(
        href = "",
        text = ResourceString.settings,
        icon = Icons.Outlined.Settings
    )
}
