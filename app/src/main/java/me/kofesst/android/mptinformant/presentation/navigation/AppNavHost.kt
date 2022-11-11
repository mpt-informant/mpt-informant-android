package me.kofesst.android.mptinformant.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.kofesst.android.mptinformant.presentation.views.releases.AppReleasesView
import me.kofesst.android.mptinformant.presentation.views.schedule.GroupInfoView

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = AppView.Schedule.route,
        modifier = modifier
    ) {
        composable(route = AppView.Schedule.route) {
            GroupInfoView(modifier = Modifier.fillMaxSize())
        }
        composable(route = AppView.Releases.route) {
            AppReleasesView(modifier = Modifier.fillMaxSize())
        }
    }
}