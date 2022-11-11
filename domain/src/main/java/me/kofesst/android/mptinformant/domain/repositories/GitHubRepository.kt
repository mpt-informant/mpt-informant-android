package me.kofesst.android.mptinformant.domain.repositories

import me.kofesst.android.mptinformant.domain.models.releases.AppRelease

interface GitHubRepository {
    suspend fun getAppReleases(): List<AppRelease>
}