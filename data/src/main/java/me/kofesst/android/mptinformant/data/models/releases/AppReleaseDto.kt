package me.kofesst.android.mptinformant.data.models.releases

import me.kofesst.android.mptinformant.domain.models.releases.AppRelease
import java.util.*

data class AppReleaseDto(
    val html_url: String,
    val tag_name: String,
    val name: String,
    val body: String,
    val created_at: Date,
) {
    fun toDomain() = AppRelease(
        url = html_url,
        tag = tag_name,
        name = name,
        body = body,
        createdAt = created_at
    )
}
