package me.kofesst.android.mptinformant.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import me.kofesst.android.mptinformant.BuildConfig
import me.kofesst.android.mptinformant.data.remote.InformantApiService
import me.kofesst.android.mptinformant.data.repositories.ChangesRepositoryImpl
import me.kofesst.android.mptinformant.data.repositories.DepartmentsRepositoryImpl
import me.kofesst.android.mptinformant.data.repositories.PreferencesRepositoryImpl
import me.kofesst.android.mptinformant.data.repositories.ScheduleRepositoryImpl
import me.kofesst.android.mptinformant.domain.repositories.ChangesRepository
import me.kofesst.android.mptinformant.domain.repositories.DepartmentsRepository
import me.kofesst.android.mptinformant.domain.repositories.PreferencesRepository
import me.kofesst.android.mptinformant.domain.repositories.ScheduleRepository
import me.kofesst.android.mptinformant.domain.usecases.UseCases
import me.kofesst.android.mptinformant.presentation.utils.OfflineInterceptor
import me.kofesst.android.mptinformant.presentation.utils.OnlineInterceptor
import me.kofesst.android.mptinformant.presentation.utils.buildOfflineInterceptor
import me.kofesst.android.mptinformant.presentation.utils.buildOnlineInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheDirectory = File(context.cacheDir, "schedule_responses")
        val cacheSize: Long = 20 * 1024 * 1024 // 20 MB
        return Cache(cacheDirectory, cacheSize)
    }

    @Provides
    @Singleton
    @OnlineInterceptor
    fun provideOnlineInterceptor(@ApplicationContext context: Context) =
        buildOnlineInterceptor(context)

    @Provides
    @Singleton
    @OfflineInterceptor
    fun provideOfflineInterceptor(@ApplicationContext context: Context) =
        buildOfflineInterceptor(context)

    @Provides
    @Singleton
    fun provideApiServiceClient(
        @OnlineInterceptor onlineInterceptor: Interceptor,
        @OfflineInterceptor offlineInterceptor: Interceptor,
        cache: Cache,
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun provideInformantApiService(apiServiceClient: OkHttpClient): InformantApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(apiServiceClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(InformantApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("app_preferences")
        }
    }

    @Provides
    @Singleton
    fun provideDepartmentsRepository(apiService: InformantApiService): DepartmentsRepository {
        return DepartmentsRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(apiService: InformantApiService): ScheduleRepository {
        return ScheduleRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideChangesRepository(apiService: InformantApiService): ChangesRepository {
        return ChangesRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(dataStore: DataStore<Preferences>): PreferencesRepository {
        return PreferencesRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        departmentsRepository: DepartmentsRepository,
        scheduleRepository: ScheduleRepository,
        changesRepository: ChangesRepository,
        preferencesRepository: PreferencesRepository,
    ): UseCases {
        return UseCases(
            departmentsRepository = departmentsRepository,
            scheduleRepository = scheduleRepository,
            changesRepository = changesRepository,
            preferencesRepository = preferencesRepository
        )
    }
}
