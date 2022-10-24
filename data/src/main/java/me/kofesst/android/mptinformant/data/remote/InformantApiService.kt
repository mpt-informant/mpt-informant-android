package me.kofesst.android.mptinformant.data.remote

import me.kofesst.android.mptinformant.data.models.changes.ChangesResponse
import me.kofesst.android.mptinformant.data.models.department.AllDepartmentsResponse
import me.kofesst.android.mptinformant.data.models.schedule.ScheduleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface InformantApiService {
    @GET("/api/departments/all")
    suspend fun getAllDepartments(): Response<AllDepartmentsResponse>

    @GET("/api/schedule/byId/{groupId}")
    suspend fun getGroupSchedule(@Path("groupId") groupId: String): Response<ScheduleResponse>

    @GET("/api/changes/byId/{groupId}")
    suspend fun getGroupChanges(@Path("groupId") groupId: String): Response<ChangesResponse>
}