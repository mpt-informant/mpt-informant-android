package me.kofesst.android.mptinformant.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val LocalAppState = compositionLocalOf<AppState> {
    error("App state not initialized")
}

@Stable
class AppState(
    val navController: NavHostController,
    val coroutineState: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
) {
    fun showSnackbar(
        message: String,
        action: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onDismiss: () -> Unit = {},
        onActionPerform: () -> Unit = {},
    ) {
        coroutineState.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = action,
                duration = duration,
                withDismissAction = withDismissAction
            )

            when (result) {
                SnackbarResult.Dismissed -> onDismiss()
                SnackbarResult.ActionPerformed -> onActionPerform()
            }
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineState: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) = AppState(
    navController = navController,
    coroutineState = coroutineState,
    snackbarHostState = snackbarHostState
)
