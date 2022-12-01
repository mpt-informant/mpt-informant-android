package me.kofesst.android.mptinformant.presentation.views.schedule.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.ui.components.SuspendValueHandler

@Composable
fun <Item : Any> SuspendItemsTab(
    modifier: Modifier = Modifier,
    items: SuspendValue<Item>,
    onRefresh: () -> Unit,
    content: @Composable (Item) -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = items.state == SuspendValue.State.Loading
        ),
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        SuspendValueHandler(
            value = items,
            content = content,
            modifier = Modifier.fillMaxSize()
        )
    }
}