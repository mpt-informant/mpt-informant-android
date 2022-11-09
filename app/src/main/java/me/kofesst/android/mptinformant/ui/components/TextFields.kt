package me.kofesst.android.mptinformant.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField as MaterialOutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign

@Composable
fun OutlinedNumericTextField(
    modifier: Modifier = Modifier,
    value: Int? = 0,
    onValueChange: (Int?) -> Unit = {},
    onFocusLeftValueFormatter: (String) -> String = { it },
    isReadOnly: Boolean = false,
    errorMessage: String? = null,
    label: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    singleLine: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    OutlinedAppTextField(
        value = value?.toString() ?: "",
        onValueChange = {
            onValueChange(
                if (it.isBlank()) {
                    null
                } else {
                    it.toIntOrNull() ?: value
                }
            )
        },
        onFocusLeftValueFormatter = onFocusLeftValueFormatter,
        isReadOnly = isReadOnly,
        errorMessage = errorMessage,
        label = label,
        leadingIcon = leadingIcon,
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        onTrailingIconClick = onTrailingIconClick,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = textStyle,
        modifier = modifier
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OutlinedAppTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onFocusLeftValueFormatter: (String) -> String = { it },
    isReadOnly: Boolean = false,
    errorMessage: String? = null,
    label: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    singleLine: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var focused by remember {
        mutableStateOf(false)
    }
    Column(modifier = modifier) {
        MaterialOutlinedTextField(
            value = if (focused) {
                value
            } else {
                onFocusLeftValueFormatter(value)
            },
            onValueChange = { onValueChange(it) },
            readOnly = isReadOnly,
            isError = errorMessage != null,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null
                    )
                }
            } else {
                null
            },
            singleLine = singleLine,
            trailingIcon = if (trailingIcon != null) {
                {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null
                        )
                    }
                }
            } else {
                null
            },
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    focused = state.isFocused
                }
        )
        TextFieldError(
            modifier = Modifier.fillMaxWidth(),
            message = errorMessage
        )
    }
}

@Composable
fun TextFieldError(
    modifier: Modifier = Modifier,
    message: String? = null,
) {
    AnimatedVisibility(visible = message != null) {
        Text(
            text = message ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Start,
            modifier = modifier
        )
    }
}
