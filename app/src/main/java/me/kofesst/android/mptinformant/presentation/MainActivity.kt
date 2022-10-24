package me.kofesst.android.mptinformant.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.presentation.views.GroupInfoView
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
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
        val appState = rememberAppState()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()
        ModalNavigationDrawer(
            gesturesEnabled = drawerState.isOpen,
            drawerState = drawerState,
            drawerContent = {
                ExtraLinksDrawerContent(
                    onNavigationButtonClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.href))
                    startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    TopAppBar {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarHostState,
                        modifier = Modifier.padding(bottom = 40.dp)
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) {
                CompositionLocalProvider(
                    LocalAppState provides appState
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
                items = ExtraLink.values().filter { it.default },
                key = { it.ordinal }
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
    ) {
        LazyColumn(modifier = modifier) {
            items(
                items = ExtraLink.values().filter { !it.default },
                key = { it.ordinal }
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
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    imageVector = Icons.Outlined.OpenInNew,
                    contentDescription = null
                )
            }
        }
    }
}
