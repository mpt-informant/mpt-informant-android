package me.kofesst.android.mptinformant.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.kofesst.android.mptinformant.presentation.utils.normalize
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.theme.color
import me.kofesst.android.mptinformant.ui.uiText
import me.kofesst.android.mptinformer.domain.models.WeekLabel
import me.kofesst.android.mptinformer.domain.models.schedule.GroupSchedule
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleDay
import me.kofesst.android.mptinformer.domain.models.schedule.GroupScheduleRow

@Composable
fun GroupScheduleColumn(
    modifier: Modifier = Modifier,
    schedule: GroupSchedule,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        item(schedule.weekLabel) {
            Surface(
                color = schedule.weekLabel.color(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(size = 20.dp))
            ) {
                Text(
                    text = schedule.weekLabel.uiText().asString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
        items(
            items = schedule.days,
            key = { it.dayOfWeek.ordinal }
        ) { day ->
            GroupScheduleDayCard(
                scheduleDay = day,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GroupScheduleDayCard(
    modifier: Modifier = Modifier,
    scheduleDay: GroupScheduleDay,
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = scheduleDay.dayOfWeek.uiText().asString().uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (scheduleDay.branch.isNotBlank()) {
                Text(
                    text = scheduleDay.branch.normalize(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light
                )
            }
            Divider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                scheduleDay.rows.forEachIndexed { index, row ->
                    GroupScheduleRow(
                        scheduleRow = row,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (index != scheduleDay.rows.lastIndex) {
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupScheduleRow(
    modifier: Modifier = Modifier,
    scheduleRow: GroupScheduleRow,
) {
    when (scheduleRow) {
        is GroupScheduleRow.Single -> {
            SingleGroupScheduleRow(
                scheduleRow = scheduleRow,
                modifier = modifier
            )
        }
        is GroupScheduleRow.Divided -> {
            DividedGroupScheduleRow(
                scheduleRow = scheduleRow,
                modifier = modifier
            )
        }
        else -> Unit
    }
}

@Composable
private fun SingleGroupScheduleRow(
    modifier: Modifier = Modifier,
    scheduleRow: GroupScheduleRow.Single,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "${scheduleRow.lessonNumber}.",
            style = MaterialTheme.typography.bodyLarge
        )
        Column(
            modifier = Modifier.weight(1.0f)
        ) {
            Text(
                text = scheduleRow.lesson,
                style = MaterialTheme.typography.bodyLarge
            )
            if (scheduleRow.teacher.isNotBlank()) {
                Text(
                    text = scheduleRow.teacher,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
private fun DividedGroupScheduleRow(
    modifier: Modifier = Modifier,
    scheduleRow: GroupScheduleRow.Divided,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = ResourceString.lessonNumberFormat.asString(scheduleRow.lessonNumber),
            style = MaterialTheme.typography.bodyLarge
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1.0f)
        ) {
            GroupScheduleDayLabel(
                weekLabel = WeekLabel.Numerator,
                label = scheduleRow.numerator,
                modifier = Modifier.fillMaxWidth()
            )
            GroupScheduleDayLabel(
                weekLabel = WeekLabel.Denominator,
                label = scheduleRow.denominator,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GroupScheduleDayLabel(
    modifier: Modifier = Modifier,
    weekLabel: WeekLabel,
    label: GroupScheduleRow.Divided.Label,
) {
    Surface(
        color = weekLabel.color(),
        modifier = modifier.clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label.lesson,
                style = MaterialTheme.typography.bodyLarge
            )
            if (label.teacher.isNotBlank()) {
                Text(
                    text = label.teacher,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}
