package me.kofesst.android.mptinformant.presentation.views.schedule.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.mptinformant.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.components.GroupChangesColumn
import me.kofesst.android.mptinformant.ui.components.IconMessage
import me.kofesst.android.mptinformant.ui.components.SuspendValueHandler

@Composable
fun GroupChangesTab(
    modifier: Modifier = Modifier,
    changes: SuspendValue<GroupChanges>,
    onChangesRefresh: () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = changes.state == SuspendValue.State.Loading
        ),
        onRefresh = onChangesRefresh,
        modifier = modifier
    ) {
        SuspendValueHandler(
            value = changes,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                it.days.isEmpty() -> {
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
                        changes = it,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}