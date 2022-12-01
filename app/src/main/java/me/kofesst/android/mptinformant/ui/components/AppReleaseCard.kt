package me.kofesst.android.mptinformant.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import me.kofesst.android.mptinformant.BuildConfig
import me.kofesst.android.mptinformant.domain.models.releases.AppRelease
import me.kofesst.android.mptinformant.ui.ResourceString

@Composable
fun AppReleaseCard(
    modifier: Modifier = Modifier,
    appRelease: AppRelease,
    isLatest: Boolean,
    onRedirectClick: () -> Unit,
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            AppReleaseCardHeader(
                name = appRelease.name,
                createdAt = appRelease.createdAt,
                tag = appRelease.tag,
                isLatest = isLatest,
                modifier = Modifier.fillMaxWidth()
            )
            if (appRelease.body.isNotBlank()) {
                Divider()
                AppReleaseCardBody(
                    body = appRelease.body,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Divider()
            TextButton(
                onClick = onRedirectClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ResourceString.openInBrowser.asString(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun AppReleaseCardHeader(
    modifier: Modifier = Modifier,
    name: String,
    createdAt: Date,
    isLatest: Boolean,
    tag: String,
) {
    Column(
        modifier = modifier
    ) {
        AppReleaseCardHeaderTitle(
            name = name,
            createdAt = createdAt,
            modifier = Modifier.fillMaxWidth()
        )
        AppReleaseCardHeaderTags(
            tag = tag,
            isLatest = isLatest,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AppReleaseCardHeaderTitle(
    modifier: Modifier = Modifier,
    name: String,
    createdAt: Date,
) {
    val dateFormatter = SimpleDateFormat(
        ResourceString.releaseDateFormat.asString(),
        Locale.ROOT
    )
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = dateFormatter.format(createdAt),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppReleaseCardHeaderTags(
    modifier: Modifier = Modifier,
    tag: String,
    isLatest: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
    ) {
        SuggestionChip(
            enabled = false,
            onClick = {},
            label = {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
        if (isCurrentVersion(tag)) {
            SuggestionChip(
                enabled = false,
                onClick = {},
                colors = SuggestionChipDefaults.suggestionChipColors(
                    disabledLabelColor = Color.Magenta
                ),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    disabledBorderColor = Color.Magenta
                ),
                label = {
                    Text(
                        text = ResourceString.currentReleaseTag.asString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
        if (isLatest) {
            SuggestionChip(
                enabled = false,
                onClick = {},
                colors = SuggestionChipDefaults.suggestionChipColors(
                    disabledLabelColor = Color.Green

                ),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    disabledBorderColor = Color.Green
                ),
                label = {
                    Text(
                        text = ResourceString.latestReleaseTag.asString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}

@Composable
private fun AppReleaseCardBody(
    modifier: Modifier = Modifier,
    body: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        body.split("\r\n").forEach { line ->
            Text(
                text = line,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun isCurrentVersion(releaseTag: String): Boolean {
    val releaseName = releaseTag.replaceFirst("v", "")
    val appVersionName = BuildConfig.VERSION_NAME
    return releaseName == appVersionName
}
