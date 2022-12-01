package me.kofesst.android.mptinformant.presentation.views.schedule.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.kofesst.android.mptinformant.domain.models.schedule.GroupSchedule
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.components.GroupScheduleColumn
import me.kofesst.android.mptinformant.ui.components.IconMessage

@Composable
fun GroupScheduleTab(
    modifier: Modifier = Modifier,
    schedule: SuspendValue<GroupSchedule>,
    onScheduleRefresh: () -> Unit,
) {
    SuspendItemsTab(
        items = schedule,
        onRefresh = onScheduleRefresh,
        modifier = modifier
    ) { groupSchedule ->
        when {
            groupSchedule.days.isEmpty() -> {
                IconMessage(
                    icon = Icons.Outlined.Mood,
                    iconTint = MaterialTheme.colorScheme.onBackground,
                    message = ResourceString.emptySchedule.asString(),
                    messageStyle = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                GroupScheduleColumn(
                    schedule = groupSchedule,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}