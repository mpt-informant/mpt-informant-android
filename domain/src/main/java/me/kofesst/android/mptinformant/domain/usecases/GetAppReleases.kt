package me.kofesst.android.mptinformant.domain.usecases

import me.kofesst.android.mptinformant.domain.repositories.GitHubRepository

class GetAppReleases(private val gitHubRepository: GitHubRepository) {
    suspend operator fun invoke() =
        gitHubRepository.getAppReleases()
}