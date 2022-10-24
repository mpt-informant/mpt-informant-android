package me.kofesst.android.mptinformant.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import me.kofesst.android.mptinformant.ui.ResourceString

enum class ExtraLink(
    val href: String,
    val text: ResourceString,
    val icon: ImageVector,
    val default: Boolean = true,
) {
    Schedule(
        href = "https://mpt.ru/studentu/raspisanie-zanyatiy/",
        text = ResourceString.schedule,
        icon = Icons.Outlined.CalendarToday
    ),
    Changes(
        href = "https://mpt.ru/studentu/izmeneniya-v-raspisanii/",
        text = ResourceString.changes,
        icon = Icons.Outlined.EditCalendar
    ),
    Departments(
        href = "https://mpt.ru/sites-otdels/",
        text = ResourceString.departmentsSite,
        icon = Icons.Outlined.Groups
    ),
    OrderCertificate(
        href = "https://student.mpt.ru/",
        text = ResourceString.orderCertificate,
        icon = Icons.Outlined.Assignment
    ),
    ExamsSchedule(
        href = "https://mpt.ru/studentu/raspisanie-ekzamenov/",
        text = ResourceString.examsSchedule,
        icon = Icons.Outlined.ReceiptLong
    ),
    Author(
        href = "https://vk.com/kofesst",
        text = ResourceString.author,
        icon = Icons.Outlined.Person,
        default = false
    ),
    GitHub(
        href = "https://github.com/mpt-informant",
        text = ResourceString.github,
        icon = Icons.Outlined.DeviceHub,
        default = false
    )
}
