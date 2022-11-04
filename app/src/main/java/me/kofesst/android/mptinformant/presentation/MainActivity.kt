@file:OptIn(ExperimentalMaterialApi::class)

package me.kofesst.android.mptinformant.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsForm
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsFormAction
import me.kofesst.android.mptinformant.presentation.views.GroupInfoView
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.components.OutlinedNumericTextField
import me.kofesst.android.mptinformant.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityContent()
                }
            }
        }
    }

    @Composable
    private fun MainActivityContent() {
        val viewModel = hiltViewModel<MainViewModel>(
            viewModelStoreOwner = this
        )
        LaunchedEffect(Unit) {
            viewModel.loadSettings()
        }

        val appState = rememberAppState()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )
        val coroutineScope = rememberCoroutineScope()
        CompositionLocalProvider(
            LocalAppState provides appState
        ) {
            AppNavigationDrawer(
                drawerState = drawerState,
                sheetState = sheetState,
                coroutineScope = coroutineScope,
                modifier = Modifier.fillMaxSize()
            ) {
                AppBottomSettingsSheet(
                    sheetState = sheetState,
                    coroutineScope = coroutineScope
                ) {
                    AppScaffold(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        GroupInfoView(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AppNavigationDrawer(
        modifier: Modifier = Modifier,
        drawerState: DrawerState,
        sheetState: ModalBottomSheetState,
        coroutineScope: CoroutineScope,
        content: @Composable () -> Unit,
    ) {
        ModalNavigationDrawer(
            gesturesEnabled = drawerState.isOpen,
            drawerState = drawerState,
            drawerContent = {
                ExtraLinksDrawerContent(
                    onNavigationButtonClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },
                    onWidgetSettingsClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            sheetState.show()
                        }
                    }
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.href))
                    startActivity(intent)
                }
            },
            content = content,
            modifier = modifier
        )
    }

    @Composable
    private fun AppBottomSettingsSheet(
        sheetState: ModalBottomSheetState,
        coroutineScope: CoroutineScope,
        content: @Composable () -> Unit,
    ) {
        val context = LocalContext.current
        val appState = LocalAppState.current
        val viewModel = hiltViewModel<MainViewModel>(
            viewModelStoreOwner = this
        )
        val widgetSettingsForm by viewModel.widgetSettingsForm
        ModalBottomSheetLayout(
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
            sheetState = sheetState,
            sheetContent = {
                BottomSettingsHeader(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                }
                WidgetSettingsSaveResultHandler(result = viewModel.widgetSettingsSubmitResult) {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    appState.showSnackbar(
                        message = ResourceString.widgetSettingsSaved.asString(context)
                    )
                }
                BottomSettingsContent(
                    widgetSettingsForm = widgetSettingsForm,
                    onFormAction = {
                        viewModel.onWidgetSettingsFormAction(it)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
            },
            content = content
        )
    }

    @Composable
    private fun AppScaffold(
        modifier: Modifier = Modifier,
        coroutineScope: CoroutineScope,
        drawerState: DrawerState,
        content: @Composable (PaddingValues) -> Unit,
    ) {
        val appState = LocalAppState.current
        Scaffold(
            topBar = {
                TopAppBar {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = appState.snackbarHostState)
            },
            content = content,
            modifier = modifier
        )
    }

    @Composable
    private fun BottomSettingsHeader(
        modifier: Modifier = Modifier,
        onExpandClick: () -> Unit,
    ) {
        ElevatedButton(
            shape = RectangleShape,
            onClick = onExpandClick,
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowDownward,
                contentDescription = null
            )
        }
    }

    @Composable
    private fun BottomSettingsContent(
        modifier: Modifier = Modifier,
        widgetSettingsForm: WidgetSettingsForm,
        onFormAction: (WidgetSettingsFormAction) -> Unit,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = modifier
        ) {
            Text(
                text = ResourceString.widgetSettings.asString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ResourceString.widgetSettingsDescription.asString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Divider()
            ScheduleWidgetTimeSettings(
                hours = widgetSettingsForm.nextDayHours,
                hoursErrorMessage = widgetSettingsForm.hoursErrorMessage,
                onHoursChange = {
                    onFormAction(
                        WidgetSettingsFormAction.NextDayHourChanged(it)
                    )
                },
                minutes = widgetSettingsForm.nextDayMinutes,
                minutesErrorMessage = widgetSettingsForm.minutesErrorMessage,
                onMinutesChange = {
                    onFormAction(
                        WidgetSettingsFormAction.NextDayMinuteChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            ScheduleWidgetLabelSettings(
                checked = widgetSettingsForm.hideLabel,
                onCheckedChange = {
                    onFormAction(
                        WidgetSettingsFormAction.HideLabelChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            ScheduleWidgetChangesSettings(
                checked = widgetSettingsForm.showChangesMessage,
                onCheckedChange = {
                    onFormAction(
                        WidgetSettingsFormAction.ShowChangesMessageChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            ExtendedFloatingActionButton(
                onClick = {
                    onFormAction(WidgetSettingsFormAction.Submit)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Сохранить изменения",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    @Composable
    private fun WidgetSettingsSaveResultHandler(
        result: Flow<Boolean>,
        onSuccess: () -> Unit,
    ) {
        LaunchedEffect(Unit) {
            result.collect {
                if (it) {
                    onSuccess()
                }
            }
        }
    }

    @Composable
    private fun SettingsColumn(
        modifier: Modifier = Modifier,
        title: String,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light
            )
            content()
        }
    }

    @Composable
    private fun CheckBoxSettings(
        modifier: Modifier = Modifier,
        text: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable(
                    indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onCheckedChange(!checked) }
                )
                .requiredHeight(ButtonDefaults.MinHeight)
                .padding(4.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    @Composable
    private fun ScheduleWidgetTimeSettings(
        modifier: Modifier = Modifier,
        hours: Int?,
        hoursErrorMessage: String?,
        onHoursChange: (Int?) -> Unit,
        minutes: Int?,
        minutesErrorMessage: String?,
        onMinutesChange: (Int?) -> Unit,
    ) {
        SettingsColumn(
            title = ResourceString.widgetTimeSettingsDescription.asString(),
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedNumericTextField(
                    value = hours,
                    onValueChange = onHoursChange,
                    onFocusLeftValueFormatter = { it.padStart(2, '0') },
                    label = ResourceString.hours.asString(),
                    errorMessage = hoursErrorMessage,
                    modifier = Modifier.weight(1.0f)
                )
                OutlinedNumericTextField(
                    value = minutes,
                    onValueChange = onMinutesChange,
                    onFocusLeftValueFormatter = { it.padStart(2, '0') },
                    label = ResourceString.minutes.asString(),
                    errorMessage = minutesErrorMessage,
                    modifier = Modifier.weight(1.0f)
                )
            }
        }
    }

    @Composable
    private fun ScheduleWidgetLabelSettings(
        modifier: Modifier = Modifier,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
    ) {
        SettingsColumn(
            title = ResourceString.widgetLabelSettingsDescription.asString(),
            modifier = modifier
        ) {
            CheckBoxSettings(
                text = ResourceString.widgetHideLabelSettings.asString(),
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }

    @Composable
    private fun ScheduleWidgetChangesSettings(
        modifier: Modifier = Modifier,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
    ) {
        SettingsColumn(
            title = ResourceString.widgetChangesSettingsDescription.asString(),
            modifier = modifier
        ) {
            CheckBoxSettings(
                text = ResourceString.widgetShowChangesMessageSettings.asString(),
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }

    @Composable
    private fun TopAppBar(
        onNavigationButtonClick: () -> Unit,
    ) {
        TopAppBar(
            title = {
                Text(
                    text = ResourceString.appName.asString(),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigationButtonClick
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = null
                    )
                }
            }
        )
    }

    @Composable
    private fun ExtraLinksDrawerContent(
        onNavigationButtonClick: () -> Unit,
        onWidgetSettingsClick: () -> Unit,
        onActionClick: (ExtraLink) -> Unit,
    ) {
        ModalDrawerSheet {
            ExtraLinksHeader(
                onNavigationButtonClick = onNavigationButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            ExtraLinksColumn(
                onActionClick = onActionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            ExtraLinksFooter(
                onActionClick = onActionClick,
                onWidgetSettingsClick = onWidgetSettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }

    @Composable
    private fun ExtraLinksHeader(
        modifier: Modifier = Modifier,
        onNavigationButtonClick: () -> Unit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            IconButton(onClick = onNavigationButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null
                )
            }
            Text(
                text = ResourceString.extraLinks.asString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    @Composable
    private fun ExtraLinksColumn(
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
    private fun ExtraLinksFooter(
        modifier: Modifier = Modifier,
        onActionClick: (ExtraLink) -> Unit,
        onWidgetSettingsClick: () -> Unit,
    ) {
        LazyColumn(modifier = modifier) {
            item {
                ExtraLinkButton(
                    extraLink = ExtraLink.WidgetSettings,
                    showHrefIcon = false,
                    onClick = onWidgetSettingsClick
                )
            }
            items(
                items = ExtraLink.additional(),
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
    private fun ExtraLinkButton(
        modifier: Modifier = Modifier,
        extraLink: ExtraLink,
        showHrefIcon: Boolean = true,
        onClick: () -> Unit,
    ) {
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
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
                    style = MaterialTheme.typography.bodyLarge
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
}
