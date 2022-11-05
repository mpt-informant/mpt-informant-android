package me.kofesst.android.mptinformant.di

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.mptinformant.widget.worker.ScheduleWorkerTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        startScheduleWorkerTask()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

    fun restartScheduleWorkerTask() {
        WorkManager.getInstance(this)
            .cancelUniqueWork(ScheduleWorkerTask.UNIQUE_WORKER_TAG)
        startScheduleWorkerTask()
    }

    private fun startScheduleWorkerTask() {
        CoroutineScope(Dispatchers.Default).launch {
            setupScheduleWorkerTask()
        }
    }

    private fun setupScheduleWorkerTask() {
        val scheduleTaskRequest = PeriodicWorkRequest.Builder(
            ScheduleWorkerTask::class.java,
            15L,
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ScheduleWorkerTask.UNIQUE_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            scheduleTaskRequest
        )
    }
}
