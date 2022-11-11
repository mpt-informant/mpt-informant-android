package me.kofesst.android.mptinformant.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.presentation.navigation.AppBottomBar
import me.kofesst.android.mptinformant.presentation.navigation.AppNavHost
import me.kofesst.android.mptinformant.presentation.sheet.AppBottomSheetContent
import me.kofesst.android.mptinformant.presentation.sheet.AppBottomSheetHeader
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.components.ExtraLinkButton
import me.kofesst.android.mptinformant.ui.components.ExtraLinksColumn
import me.kofesst.android.mptinformant.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val CHANGES_RECEIVER_ACTION = "NEW_CHANGES"
    }

    private val _changesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent?.action == null) return
            val text = intent.getStringExtra("data") ?: ""
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(CHANGES_RECEIVER_ACTION)
        val receiverFlags = ContextCompat.RECEIVER_NOT_EXPORTED
        ContextCompat.registerReceiver(this, _changesReceiver, filter, receiverFlags)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(_changesReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<MainViewModel>(
                viewModelStoreOwner = this
            )
            val appSettings by viewModel.appSettings
            val weekLabel by viewModel.weekLabel
            LaunchedEffect(Unit) {
                viewModel.loadSettings()
            }
            AppTheme(
                useWeekLabelTheme = appSettings.useWeekLabelTheme,
                weekLabel = weekLabel
            ) {
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
                        AppNavHost(
                            navController = appState.navController,
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
                AppDrawerContent(
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
        val appSettingsForm by viewModel.appSettingsForm
        ModalBottomSheetLayout(
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
            sheetState = sheetState,
            sheetContent = {
                WidgetSettingsResultHandler(result = viewModel.settingsSubmitResult) {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    appState.showSnackbar(
                        message = ResourceString.settingsSaved.asString(context)
                    )
                }
                AppBottomSheetHeader(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                }
                AppBottomSheetContent(
                    widgetSettingsForm = widgetSettingsForm,
                    onWidgetSettingsFormAction = {
                        viewModel.onWidgetSettingsFormAction(it)
                    },
                    appSettingsForm = appSettingsForm,
                    onAppSettingsFormAction = {
                        viewModel.onAppSettingsFormAction(it)
                    },
                    onSubmitClick = {
                        viewModel.submitSettings()
                    },
                    modifier = Modifier.fillMaxSize()
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
        val navController = appState.navController
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Scaffold(
            topBar = {
                TopAppBar {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            },
            bottomBar = {
                AppBottomBar(
                    currentScreenRoute = currentRoute,
                    navController = navController,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = appState.snackbarHostState)
            },
            content = content,
            modifier = modifier
        )
    }

    @Composable
    private fun WidgetSettingsResultHandler(
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
    private fun AppDrawerContent(
        onNavigationButtonClick: () -> Unit,
        onWidgetSettingsClick: () -> Unit,
        onActionClick: (ExtraLink) -> Unit,
    ) {
        ModalDrawerSheet {
            AppDrawerHeader(
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
            AppDrawerFooter(
                onActionClick = onActionClick,
                onWidgetSettingsClick = onWidgetSettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }

    @Composable
    private fun AppDrawerHeader(
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
    private fun AppDrawerFooter(
        modifier: Modifier = Modifier,
        onActionClick: (ExtraLink) -> Unit,
        onWidgetSettingsClick: () -> Unit,
    ) {
        LazyColumn(modifier = modifier) {
            item {
                ExtraLinkButton(
                    extraLink = ExtraLink.Settings,
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
}
