package me.kofesst.android.mptinformant.presentation.utils

import android.util.Log
import androidx.compose.runtime.MutableState

data class SuspendValue<T : Any>(
    val value: T? = null,
    val state: State = State.Idle,
) {
    sealed class State {
        object Idle : State()
        object Loading : State()
        object Success : State()
        data class Failed(val throwable: Throwable) : State()
    }
}

suspend fun <T : Any> MutableState<SuspendValue<T>>.loadSuspend(
    block: suspend () -> T?,
) {
    value = value.copy(
        state = SuspendValue.State.Loading
    )
    value = try {
        value.copy(
            value = block(),
            state = SuspendValue.State.Success
        )
    } catch (exception: Exception) {
        Log.e("SuspendValueLoading", exception.stackTraceToString())
        value.copy(
            state = SuspendValue.State.Failed(exception)
        )
    }
}
