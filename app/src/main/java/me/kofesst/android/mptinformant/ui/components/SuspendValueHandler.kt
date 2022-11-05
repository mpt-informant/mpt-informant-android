package me.kofesst.android.mptinformant.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.ui.ResourceString

@Composable
fun <T : Any> SuspendValueHandler(
    modifier: Modifier = Modifier,
    value: SuspendValue<T>,
    content: @Composable (T) -> Unit,
) {
    val loadedValue = value.value
    Box(modifier = modifier) {
        if (value.state is SuspendValue.State.Failed) {
            IconMessage(
                icon = Icons.Outlined.Error,
                iconTint = MaterialTheme.colorScheme.error,
                message = ResourceString.unexpectedError.asString(),
                messageStyle = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            if (loadedValue != null) {
                content(loadedValue)
            }
        }
    }
}
