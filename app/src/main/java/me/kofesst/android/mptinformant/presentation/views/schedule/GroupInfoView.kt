package me.kofesst.android.mptinformant.presentation.views.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.di.App
import me.kofesst.android.mptinformant.domain.models.Department
import me.kofesst.android.mptinformant.domain.models.Group
import me.kofesst.android.mptinformant.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformant.domain.models.schedule.GroupSchedule
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.presentation.utils.normalize
import me.kofesst.android.mptinformant.presentation.views.schedule.tabs.GroupChangesTab
import me.kofesst.android.mptinformant.presentation.views.schedule.tabs.GroupScheduleTab
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.components.*
import me.kofesst.android.mptinformant.ui.uiText

@Composable
fun GroupInfoView(
    modifier: Modifier = Modifier,
    viewModel: GroupInfoViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState
    LaunchedEffect(Unit) {
        if (!viewState.isValid) {
            viewModel.initializeViewState()
        }
    }

    val scheduleState by viewModel.scheduleState
    val changesState by viewModel.changesState
    if (viewState.isValid) {
        GroupInfoViewContent(
            viewState = viewState,
            schedule = scheduleState,
            onScheduleRefresh = viewModel::refreshSchedule,
            changes = changesState,
            onChangesRefresh = viewModel::refreshChanges,
            onDepartmentChange = viewModel::setDepartment,
            onGroupChange = viewModel::setGroup,
            modifier = modifier
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun GroupInfoViewContent(
    modifier: Modifier = Modifier,
    viewState: GroupInfoViewState,
    schedule: SuspendValue<GroupSchedule>,
    onScheduleRefresh: () -> Unit,
    changes: SuspendValue<GroupChanges>,
    onChangesRefresh: () -> Unit,
    onDepartmentChange: (Department) -> Unit,
    onGroupChange: (Group) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = GroupInfoViewTab.values().size)
    val (viewTab, setViewTab) = remember {
        mutableStateOf(GroupInfoViewTab.Schedule)
    }
    Column(
        modifier = modifier
    ) {
        PageTabs(
            pagerState = pagerState,
            tab = viewTab,
            onTabChange = setViewTab
        )
        TabsPager(
            pagerState = pagerState,
            schedule = schedule,
            onScheduleRefresh = onScheduleRefresh,
            changes = changes,
            onChangesRefresh = onChangesRefresh,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        )
        SettingsPanel(
            viewState = viewState,
            onDepartmentChange = onDepartmentChange,
            onGroupChange = onGroupChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PageTabs(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tab: GroupInfoViewTab,
    onTabChange: (GroupInfoViewTab) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier
    ) {
        GroupInfoViewTab.values().forEachIndexed { index, value ->
            Tab(
                selected = value == tab,
                text = {
                    Text(
                        text = value.uiText().asString().normalize(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                onClick = {
                    onTabChange(value)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TabsPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    schedule: SuspendValue<GroupSchedule>,
    onScheduleRefresh: () -> Unit,
    changes: SuspendValue<GroupChanges>,
    onChangesRefresh: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) { pageIndex ->
        when (GroupInfoViewTab.values()[pageIndex]) {
            GroupInfoViewTab.Schedule -> {
                GroupScheduleTab(
                    schedule = schedule,
                    onScheduleRefresh = onScheduleRefresh,
                    modifier = Modifier.fillMaxSize()
                )
            }

            GroupInfoViewTab.Changes -> {
                GroupChangesTab(
                    changes = changes,
                    onChangesRefresh = onChangesRefresh,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun SettingsPanel(
    modifier: Modifier = Modifier,
    viewState: GroupInfoViewState,
    onDepartmentChange: (Department) -> Unit,
    onGroupChange: (Group) -> Unit,
) {
    val application = LocalContext.current.applicationContext as App
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Dropdown(
            items = viewState.departmentsList.map { department ->
                DropdownItem(
                    text = department.name,
                    onSelected = {
                        onDepartmentChange(department)
                        application.restartScheduleWorkerTask()
                    }
                )
            },
            value = viewState.department!!.name,
            placeholder = ResourceString.department.asString(),
            modifier = Modifier.weight(1.0f),
        )
        Dropdown(
            items = viewState.department.groups.map { group ->
                DropdownItem(
                    text = group.name,
                    onSelected = {
                        onGroupChange(group)
                        application.restartScheduleWorkerTask()
                    }
                )
            },
            value = viewState.group!!.name,
            placeholder = ResourceString.group.asString(),
            modifier = Modifier.weight(1.0f),
        )
    }
}
