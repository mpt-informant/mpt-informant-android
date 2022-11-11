package me.kofesst.android.mptinformant.domain.models.releases

import java.util.*

data class AppRelease(
    val url: String,
    val tag: String,
    val name: String,
    val body: String,
    val createdAt: Date,
)
