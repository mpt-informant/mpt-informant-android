package me.kofesst.android.mptinformant.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    items: List<DropdownItem>,
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    errorMessage: String? = null,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = value,
                onValueChange = { },
                maxLines = 1,
                singleLine = true,
                isError = errorMessage != null,
                label = { Text(text = placeholder) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            item.onSelected()
                            expanded = false
                        },
                        text = {
                            Text(
                                text = item.text,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }
        TextFieldError(
            modifier = Modifier.fillMaxWidth(),
            message = errorMessage
        )
    }
}

data class DropdownItem(
    val text: String,
    val onSelected: () -> Unit,
)
