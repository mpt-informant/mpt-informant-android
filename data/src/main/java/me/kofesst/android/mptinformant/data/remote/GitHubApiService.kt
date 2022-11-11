package me.kofesst.android.mptinformant.data.remote

import me.kofesst.android.mptinformant.data.models.releases.AppReleaseDto
import retrofit2.http.GET

interface GitHubApiService {
    @GET("/repos/mpt-informant/mpt-informant-android/releases")
    suspend fun getReleases(): List<AppReleaseDto>
}