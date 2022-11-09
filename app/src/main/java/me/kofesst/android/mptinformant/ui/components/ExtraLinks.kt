package me.kofesst.android.mptinformant.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import me.kofesst.android.mptinformant.presentation.ExtraLink

@Composable
fun ExtraLinksColumn(
    modifier: Modifier = Modifier,
    onActionClick: (ExtraLink) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = ExtraLink.default(),
            key = { it.href }
        ) { extraLink ->
            ExtraLinkButton(
                extraLink = extraLink,
                onClick = { onActionClick(extraLink) }
            )
        }
    }
}

@Composable
fun ExtraLinkButton(
    modifier: Modifier = Modifier,
    extraLink: ExtraLink,
    showHrefIcon: Boolean = true,
    onClick: () -> Unit,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = extraLink.icon,
                contentDescription = null
            )
            Text(
                text = extraLink.text.asString(),
                style = textStyle
            )
            if (showHrefIcon) {
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    imageVector = Icons.Outlined.OpenInNew,
                    contentDescription = null
                )
            }
        }
    }
}
