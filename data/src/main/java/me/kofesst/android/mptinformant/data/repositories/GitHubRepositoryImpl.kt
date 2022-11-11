package me.kofesst.android.mptinformant.data.repositories

import me.kofesst.android.mptinformant.data.remote.GitHubApiService
import me.kofesst.android.mptinformant.domain.models.releases.AppRelease
import me.kofesst.android.mptinformant.domain.repositories.GitHubRepository

class GitHubRepositoryImpl(
    private val gitHubApiService: GitHubApiService,
) : GitHubRepository {
    override suspend fun getAppReleases(): List<AppRelease> =
        gitHubApiService.getReleases().map { it.toDomain() }
}