package me.kofesst.android.mptinformant.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.kofesst.android.mptinformant.presentation.utils.normalize
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.dateUiText
import me.kofesst.android.mptinformant.ui.uiText
import me.kofesst.android.mptinformer.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformer.domain.models.changes.GroupChangesDay
import me.kofesst.android.mptinformer.domain.models.changes.GroupChangesRow

@Composable
fun GroupChangesColumn(
    modifier: Modifier = Modifier,
    changes: GroupChanges,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        items(changes.days) { changesDay ->
            GroupChangesDayCard(
                changesDay = changesDay,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GroupChangesDayCard(
    modifier: Modifier = Modifier,
    changesDay: GroupChangesDay,
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = changesDay.timestamp.dateUiText(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = changesDay.dayOfWeek.uiText().asString().normalize(),
                style = MaterialTheme.typography.bodyLarge
            )
            Divider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                changesDay.rows.forEachIndexed { index, changesRow ->
                    GroupChangesRow(
                        changesRow = changesRow,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (index != changesDay.rows.lastIndex) {
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupChangesRow(
    modifier: Modifier = Modifier,
    changesRow: GroupChangesRow,
) {
    when (changesRow) {
        is GroupChangesRow.Additional -> AdditionalGroupChangesRow(modifier, changesRow)
        is GroupChangesRow.Replace -> ReplaceGroupChangesRow(modifier, changesRow)
    }
}

@Composable
private fun AdditionalGroupChangesRow(
    modifier: Modifier = Modifier,
    changesRow: GroupChangesRow.Additional,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = ResourceString.lessonNumberFormat.asString(changesRow.lessonNumber),
            style = MaterialTheme.typography.bodyLarge
        )
        Column {
            Text(
                text = ResourceString.additionalLesson.asString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = changesRow.replacementLesson,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
private fun ReplaceGroupChangesRow(
    modifier: Modifier = Modifier,
    changesRow: GroupChangesRow.Replace,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = ResourceString.lessonNumberFormat.asString(changesRow.lessonNumber),
            style = MaterialTheme.typography.bodyLarge
        )
        Column {
            Text(
                text = changesRow.replacedLesson,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    imageVector = Icons.Outlined.ArrowDownward,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.weight(1.0f))
            }
            Text(
                text = changesRow.replacementLesson,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light
            )
        }
    }
}
