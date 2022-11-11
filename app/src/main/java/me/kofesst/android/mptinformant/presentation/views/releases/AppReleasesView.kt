package me.kofesst.android.mptinformant.presentation.views.releases

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.ui.components.AppReleaseCard
import me.kofesst.android.mptinformant.ui.components.SuspendValueHandler

@Composable
fun AppReleasesView(
    modifier: Modifier = Modifier,
    viewModel: AppReleasesViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.requestAppReleases()
    }

    val context = LocalContext.current
    val releasesState by viewModel.releasesState
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = releasesState.state == SuspendValue.State.Loading
        ),
        onRefresh = {
            viewModel.requestAppReleases()
        },
        modifier = modifier
    ) {
        SuspendValueHandler(
            value = releasesState,
            modifier = Modifier.fillMaxSize()
        ) { releases ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                itemsIndexed(releases) { index, release ->
                    AppReleaseCard(
                        appRelease = release,
                        isLatest = index == 0,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        redirectToRelease(
                            context = context,
                            url = release.url
                        )
                    }
                }
            }
        }
    }
}

private fun redirectToRelease(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    ContextCompat.startActivity(context, intent, null)
}
