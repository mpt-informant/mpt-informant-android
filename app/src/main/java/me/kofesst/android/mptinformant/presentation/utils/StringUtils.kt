package me.kofesst.android.mptinformant.presentation.utils

fun String.normalize() = when {
    isBlank() -> this
    length == 1 -> this.uppercase()
    else -> "${this[0].uppercase()}${this.substring(1).lowercase()}"
}
