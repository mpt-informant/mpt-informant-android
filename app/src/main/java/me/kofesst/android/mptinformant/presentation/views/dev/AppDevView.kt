package me.kofesst.android.mptinformant.presentation.views.dev

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.ui.components.AppReleaseCard
import me.kofesst.android.mptinformant.ui.components.SuspendValueHandler

@Composable
fun AppDevView(
    modifier: Modifier = Modifier,
    viewModel: AppDevViewModel = hiltViewModel(),
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
                item(key = "waka-time") {
                    AppWakaTimeBadge(
                        modifier = Modifier
                            .height(30.dp)
                            .fillMaxWidth()
                    )
                }
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

@Composable
private fun AppWakaTimeBadge(modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .decoderFactory(SvgDecoder.Factory())
            .data("https://wakatime.com/badge/user/1fb0c696-5526-4ebf-837f-4d5b13f48f0a/project/ffaf3d1b-4dab-481a-8b0f-501dfaa3625b.svg")
            .build()
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}

private fun redirectToRelease(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    ContextCompat.startActivity(context, intent, null)
}