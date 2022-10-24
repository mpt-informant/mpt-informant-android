package me.kofesst.android.mptinformant.presentation.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.presentation.utils.normalize
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.components.*
import me.kofesst.android.mptinformant.ui.uiText
import me.kofesst.android.mptinformer.domain.models.Department
import me.kofesst.android.mptinformer.domain.models.Group
import me.kofesst.android.mptinformer.domain.models.changes.GroupChanges
import me.kofesst.android.mptinformer.domain.models.schedule.GroupSchedule

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
                        text = value.uiText().normalize(),
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
                ScheduleTabContent(
                    schedule = schedule,
                    onScheduleRefresh = onScheduleRefresh,
                    modifier = Modifier.fillMaxSize()
                )
            }
            GroupInfoViewTab.Changes -> {
                ChangesTabContent(
                    changes = changes,
                    onChangesRefresh = onChangesRefresh,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ScheduleTabContent(
    modifier: Modifier = Modifier,
    schedule: SuspendValue<GroupSchedule>,
    onScheduleRefresh: () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = schedule.state == SuspendValue.State.Loading
        ),
        onRefresh = onScheduleRefresh,
        modifier = modifier
    ) {
        SuspendValueHandler(
            value = schedule,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                it.days.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        IconMessage(
                            icon = Icons.Outlined.Mood,
                            iconTint = MaterialTheme.colorScheme.onBackground,
                            message = ResourceString.emptySchedule.asString(),
                            messageStyle = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                else -> {
                    GroupScheduleColumn(
                        schedule = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ChangesTabContent(
    modifier: Modifier = Modifier,
    changes: SuspendValue<GroupChanges>,
    onChangesRefresh: () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = changes.state == SuspendValue.State.Loading
        ),
        onRefresh = onChangesRefresh,
        modifier = modifier
    ) {
        SuspendValueHandler(
            value = changes
        ) {
            when {
                it.days.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        IconMessage(
                            icon = Icons.Outlined.Mood,
                            iconTint = MaterialTheme.colorScheme.onBackground,
                            message = ResourceString.emptyChanges.asString(),
                            messageStyle = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                else -> {
                    GroupChangesColumn(
                        changes = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
                    }
                )
            },
            value = viewState.group!!.name,
            placeholder = ResourceString.group.asString(),
            modifier = Modifier.weight(1.0f),
        )
    }
}
