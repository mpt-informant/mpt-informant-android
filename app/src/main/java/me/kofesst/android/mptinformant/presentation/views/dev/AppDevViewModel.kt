package me.kofesst.android.mptinformant.presentation.views.dev

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.domain.models.releases.AppRelease
import me.kofesst.android.mptinformant.domain.usecases.UseCases
import me.kofesst.android.mptinformant.presentation.utils.SuspendValue
import me.kofesst.android.mptinformant.presentation.utils.loadSuspend
import javax.inject.Inject

@HiltViewModel
class AppDevViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _releasesState = mutableStateOf<SuspendValue<List<AppRelease>>>(SuspendValue())
    val releasesState: State<SuspendValue<List<AppRelease>>> get() = _releasesState

    fun requestAppReleases() {
        viewModelScope.launch {
            _releasesState.loadSuspend {
                useCases.getAppReleases()
            }
        }
    }
}