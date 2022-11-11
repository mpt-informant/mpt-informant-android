package me.kofesst.android.mptinformant.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    currentScreenRoute: String?,
    navController: NavController,
) {
    NavigationBar(
        tonalElevation = 5.dp,
        modifier = modifier
    ) {
        AppView.values().forEach { view ->
            val active = view.route == currentScreenRoute
            NavigationBarItem(
                selected = active,
                icon = {
                    Icon(
                        imageVector = view.icon,
                        contentDescription = view.title.asString()
                    )
                },
                label = {
                    Text(
                        text = view.title.asString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = {
                    if (active) return@NavigationBarItem
                    navController.navigate(view.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
