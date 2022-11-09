package me.kofesst.android.mptinformant.presentation.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.presentation.settings.AppSettingsForm
import me.kofesst.android.mptinformant.presentation.settings.AppSettingsFormAction
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsForm
import me.kofesst.android.mptinformant.presentation.settings.WidgetSettingsFormAction
import me.kofesst.android.mptinformant.presentation.utils.normalize
import me.kofesst.android.mptinformant.ui.ResourceString
import me.kofesst.android.mptinformant.ui.uiText

@Composable
fun AppBottomSheetHeader(
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppBottomSheetContent(
    modifier: Modifier = Modifier,
    widgetSettingsForm: WidgetSettingsForm,
    onWidgetSettingsFormAction: (WidgetSettingsFormAction) -> Unit,
    appSettingsForm: AppSettingsForm,
    onAppSettingsFormAction: (AppSettingsFormAction) -> Unit,
    onSubmitClick: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = AppBottomSheet.values().size)
    val (viewTab, setViewTab) = remember {
        mutableStateOf(AppBottomSheet.AppSettings)
    }
    Column(modifier = modifier) {
        SheetTabs(
            pagerState = pagerState,
            tab = viewTab,
            onTabChange = setViewTab
        )
        SheetTabsPager(
            pagerState = pagerState,
            widgetSettingsForm = widgetSettingsForm,
            onWidgetSettingsFormAction = onWidgetSettingsFormAction,
            appSettingsForm = appSettingsForm,
            onAppSettingsFormAction = onAppSettingsFormAction,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        SubmitSettingsButton(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SubmitSettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    ExtendedFloatingActionButton(
        onClick = {
            keyboardController?.hide()
            onClick()
        },
        modifier = modifier
    ) {
        Text(
            text = ResourceString.saveChanges.asString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SheetTabs(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tab: AppBottomSheet,
    onTabChange: (AppBottomSheet) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier
    ) {
        AppBottomSheet.values().forEachIndexed { index, value ->
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
private fun SheetTabsPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    widgetSettingsForm: WidgetSettingsForm,
    onWidgetSettingsFormAction: (WidgetSettingsFormAction) -> Unit,
    appSettingsForm: AppSettingsForm,
    onAppSettingsFormAction: (AppSettingsFormAction) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) { pageIndex ->
        when (AppBottomSheet.values()[pageIndex]) {
            AppBottomSheet.AppSettings -> {
                AppSettingsColumn(
                    appSettingsForm = appSettingsForm,
                    onFormAction = onAppSettingsFormAction,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AppBottomSheet.WidgetSettings -> {
                WidgetSettingsColumn(
                    widgetSettingsForm = widgetSettingsForm,
                    onFormAction = onWidgetSettingsFormAction,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SettingsColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier.padding(20.dp)
    ) {
        content()
    }
}

@Composable
fun SettingsFieldColumn(
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
fun CheckBoxSettingsField(
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
fun SettingsColumnHeader(
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    subtitle: String? = null,
    subtitleStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    Text(
        text = title,
        style = titleStyle,
        fontWeight = FontWeight.Bold
    )
    if (subtitle?.isNotBlank() == true) {
        Text(
            text = subtitle,
            style = subtitleStyle
        )
    }
}