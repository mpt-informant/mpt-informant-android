package me.kofesst.android.mptinformant.presentation.views.schedule.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.kofesst.android.mptinformant.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.components.GroupChangesColumn
import me.kofesst.android.mptinformant.ui.components.IconMessage

@Composable
fun GroupChangesTab(
    modifier: Modifier = Modifier,
    changes: SuspendValue<GroupChanges>,
    onChangesRefresh: () -> Unit,
) {
    SuspendItemsTab(
        items = changes,
        onRefresh = onChangesRefresh,
        modifier = modifier
    ) { groupChanges ->
        when {
            groupChanges.days.isEmpty() -> {
                IconMessage(
                    icon = Icons.Outlined.Mood,
                    iconTint = MaterialTheme.colorScheme.onBackground,
                    message = ResourceString.emptyChanges.asString(),
                    messageStyle = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                GroupChangesColumn(
                    changes = groupChanges,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}