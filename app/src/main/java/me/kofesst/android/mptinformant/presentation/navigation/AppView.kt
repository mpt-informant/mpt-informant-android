package me.kofesst.android.mptinformant.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import me.kofesst.android.mptinformant.ui.ResourceString

enum class AppView(val route: String, val title: ResourceString, val icon: ImageVector) {
    Schedule(
        route = "schedule",
        title = ResourceString.schedule,
        icon = Icons.Outlined.Schedule
    ),
    Dev(
        route = "dev",
        title = ResourceString.dev,
        icon = Icons.Outlined.Devices
    )
}